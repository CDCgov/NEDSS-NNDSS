package gov.cdc.nnddatapollservice.srte.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.TimestampUtil;
import gov.cdc.nnddatapollservice.srte.dao.SrteDataPersistentDAO;
import gov.cdc.nnddatapollservice.srte.service.interfaces.ISrteDataHandlingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;
import static gov.cdc.nnddatapollservice.share.TimestampUtil.getCurrentTimestamp;

@Service
@Slf4j
public class SrteDataHandlingService implements ISrteDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(SrteDataHandlingService.class);
    @Value("${datasync.store_in_local}")
    protected boolean storeJsonInLocalFolder = false;
    @Value("${datasync.store_in_S3}")
    protected boolean storeJsonInS3 = false;
    @Value("${datasync.store_in_sql}")
    protected boolean storeInSql = false;
    @Value("${datasync.data_sync_batch_limit}")
    protected Integer pullLimit = 0;
    @Value("${datasync.data_sync_delete_on_initial}")
    protected boolean deleteOnInit = false;
    private final SrteDataPersistentDAO srteDataPersistentDAO;
    private final IPollCommonService outboundPollCommonService;
    private final IS3DataService is3DataService;

    public SrteDataHandlingService(
            SrteDataPersistentDAO srteDataPersistentDAO,
            IPollCommonService outboundPollCommonService,
            IS3DataService is3DataService) {
        this.srteDataPersistentDAO = srteDataPersistentDAO;
        this.outboundPollCommonService = outboundPollCommonService;
        this.is3DataService = is3DataService;
    }

    public void handlingExchangedData() throws DataPollException {
        List<PollDataSyncConfig> configTableList = outboundPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> srteTablesList = outboundPollCommonService.getTablesConfigListBySOurceDB(configTableList, SRTE);

        boolean isInitialLoad = outboundPollCommonService.checkPollingIsInitailLoad(srteTablesList);
        logger.info("SRTE INITIAL LOAD: {}", isInitialLoad);
        //Delete the existing records
        if (isInitialLoad && storeInSql && deleteOnInit) {
            cleanupTables(srteTablesList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : srteTablesList) {
            pollAndPersistSRTEData(pollDataSyncConfig.getTableName(), isInitialLoad);
        }

    }

    @SuppressWarnings("java:S1141")
    protected void pollAndPersistSRTEData(String tableName, boolean isInitialLoad) {
        try {
            LogResponseModel log = null;
            boolean exceptionAtApiLevel = false;
            String timeStampForPoll = getPollTimestamp( isInitialLoad, tableName);
            Integer totalRecordCounts = 0;

            var timestampWithNull = TimestampUtil.getCurrentTimestamp();
            var startTime = getCurrentTimestamp();

            //call data exchange service api
            try {
                totalRecordCounts = outboundPollCommonService.callDataCountEndpoint(tableName, isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                log = new LogResponseModel(CRITICAL_COUNT_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }
            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            try {
                var encodedDataWithNull = outboundPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, "0", "0");
                var rawJsonDataWithNull = outboundPollCommonService.decodeAndDecompress(encodedDataWithNull);
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonDataWithNull, tableName, timestampWithNull, isInitialLoad);
                    log.setStartTime(startTime);
                    log.setLog(S3_LOG + log.getLog());
                    outboundPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestampWithNull, log);
                }
                else if (storeInSql) {
                    log = srteDataPersistentDAO.saveSRTEData(tableName, rawJsonDataWithNull);
                    log.setStartTime(startTime);
                    log.setLog(SQL_LOG + log.getLog());
                    outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestampWithNull, log);
                }
                else  {
                    log = outboundPollCommonService.writeJsonDataToFile(RDB, tableName, timestampWithNull, rawJsonDataWithNull);
                    log.setStartTime(startTime);
                    log.setLog(LOCAL_DIR_LOG + log.getLog());
                    outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                }
            } catch (Exception e) {
                log =  new LogResponseModel();
                log.setStatus(ERROR);
                log.setLog(CRITICAL_NULL_LOG + e.getMessage());
                log.setStackTrace(getStackTraceAsString(e));
                log.setStartTime(startTime);
                outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }


            String logStr = null;
            for (int i = 0; i < totalPages; i++) {
                String rawJsonData = "";
                Timestamp timestamp = null;

                try {
                    int startRow = i * batchSize + 1;
                    int endRow = (i + 1) * batchSize;

                    String encodedData = "";
                    encodedData = outboundPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, false, String.valueOf(startRow), String.valueOf(endRow));
                    rawJsonData = outboundPollCommonService.decodeAndDecompress(encodedData);
                    timestamp = TimestampUtil.getCurrentTimestamp();
                } catch (Exception e) {
                    logStr = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, tableName, timestamp,
                        rawJsonData, isInitialLoad, logStr, startTime);

            }
        } catch (Exception e) {
            logger.error("TASK failed. tableName: {}, message: {}", tableName, e.getMessage());
        }

    }

    protected String getPollTimestamp(boolean isInitialLoad, String tableName) {
        String timeStampForPoll;
        if (isInitialLoad) {
            timeStampForPoll = outboundPollCommonService.getCurrentTimestamp();
        } else {
            if (storeInSql) {
                timeStampForPoll = outboundPollCommonService.getLastUpdatedTime(tableName);
            }
            else if (storeJsonInS3) {
                timeStampForPoll = outboundPollCommonService.getLastUpdatedTimeS3(tableName);
            }
            else {
                timeStampForPoll = outboundPollCommonService.getLastUpdatedTimeLocalDir(tableName);
            }
        }
        return timeStampForPoll;
    }

    protected void updateDataHelper(boolean exceptionAtApiLevel, String tableName, Timestamp timestamp,
                                    String rawJsonData, boolean isInitialLoad, String log, Timestamp startTime) {
        LogResponseModel logResponseModel = new LogResponseModel();

        try {
            if (exceptionAtApiLevel)
            {
                logResponseModel.setStatus(ERROR);
                logResponseModel.setStartTime(startTime);
                logResponseModel.setLog(API_LEVEL + log);

                if (storeInSql) {
                    outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, logResponseModel);
                }
                else if (storeJsonInS3) {
                    outboundPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp,  logResponseModel);
                }
                else {
                    outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
                }
            }
            else
            {
                if (storeJsonInS3) {
                    logResponseModel = is3DataService.persistToS3MultiPart(RDB, rawJsonData, tableName, timestamp, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(S3_LOG + log);
                    outboundPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, logResponseModel);
                }
                else if (storeInSql) {
                    logResponseModel =  srteDataPersistentDAO.saveSRTEData(tableName, rawJsonData);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(SQL_LOG + log);
                    outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, logResponseModel);
                }
                else {
                    logResponseModel = outboundPollCommonService.writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(LOCAL_DIR_LOG + log);
                    outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
                }
            }
        } catch (Exception e) {
            logResponseModel = new LogResponseModel();
            logResponseModel.setStatus(ERROR);
            logResponseModel.setStartTime(startTime);
            logResponseModel.setLog(CRITICAL_NON_NULL_LOG + e.getMessage());
            logResponseModel.setStackTrace(getStackTraceAsString(e));
            outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            srteDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}