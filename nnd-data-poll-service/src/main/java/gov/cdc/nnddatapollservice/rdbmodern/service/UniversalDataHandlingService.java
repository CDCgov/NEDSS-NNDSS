package gov.cdc.nnddatapollservice.rdbmodern.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdbmodern.dao.RdbModernDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdbmodern.service.interfaces.IUniversalDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.TimestampUtil;
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
public class UniversalDataHandlingService implements IUniversalDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(UniversalDataHandlingService.class);
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
    private final RdbModernDataPersistentDAO rdbModernDataPersistentDAO;
    private final IPollCommonService iPollCommonService;
    private final IS3DataService is3DataService;

    public UniversalDataHandlingService(RdbModernDataPersistentDAO rdbModernDataPersistentDAO,
                                        IPollCommonService iPollCommonService,
                                        IS3DataService is3DataService) {
        this.rdbModernDataPersistentDAO = rdbModernDataPersistentDAO;
        this.iPollCommonService = iPollCommonService;
        this.is3DataService = is3DataService;
    }

    public void handlingExchangedData(String source) throws DataPollException {
        var startTime = getCurrentTimestamp();
        List<PollDataSyncConfig> configTableList = iPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> filteredTablesList = iPollCommonService.getTablesConfigListBySOurceDB(configTableList, source);

        boolean isInitialLoad = iPollCommonService.checkPollingIsInitailLoad(filteredTablesList);

        if (isInitialLoad && storeInSql && deleteOnInit) {
            logger.info("CLEANING UP THE TABLES ");
            cleanupTables(filteredTablesList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : filteredTablesList) {
            pollAndPersistRDBMOdernData(source, pollDataSyncConfig.getTableName(), isInitialLoad,
                    pollDataSyncConfig.getKeyList(), pollDataSyncConfig.isRecreateApplied(), startTime);
        }

    }

    @SuppressWarnings({"java:S1141","java:S3776"})
    protected void pollAndPersistRDBMOdernData(String source, String tableName, boolean isInitialLoad, String keyList,
                                               boolean recreatedApplied, Timestamp startTime)  {
        try {
            if (recreatedApplied && storeInSql) {
                rdbModernDataPersistentDAO.deleteTable(tableName);
            }
            LogResponseModel log = null;
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts = 0;
            String timeStampForPoll = getPollTimestamp(isInitialLoad, tableName);

            var timestampWithNull = getCurrentTimestamp();
            try {
                totalRecordCounts = iPollCommonService.callDataCountEndpoint(tableName, isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                log = new LogResponseModel(CRITICAL_COUNT_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }


            if (!recreatedApplied) {
                try {
                    var encodedDataWithNull = iPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, "0", "0");
                    var rawJsonDataWithNull = iPollCommonService.decodeAndDecompress(encodedDataWithNull);
                    if (storeJsonInS3) {
                        log = is3DataService.persistToS3MultiPart(source, rawJsonDataWithNull, tableName, timestampWithNull, isInitialLoad);
                        log.setStartTime(startTime);
                        log.setLog(S3_LOG + log.getLog());
                        iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestampWithNull, log);
                    }
                    else if (storeInSql) {
                        log =  rdbModernDataPersistentDAO.saveRdbModernData(tableName, rawJsonDataWithNull, keyList,
                                isInitialLoad);
                        log.setStartTime(startTime);
                        log.setLog(SQL_LOG + log.getLog());
                        iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestampWithNull, log);
                    }
                    else  {
                        log = iPollCommonService.writeJsonDataToFile(source, tableName, timestampWithNull, rawJsonDataWithNull);
                        log.setStartTime(startTime);
                        log.setLog(LOCAL_DIR_LOG + log.getLog());
                        iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                    }
                }
                catch (Exception e) {
                    log =  new LogResponseModel();
                    log.setStatus(ERROR);
                    log.setLog(CRITICAL_NULL_LOG + e.getMessage());
                    log.setStackTrace(getStackTraceAsString(e));
                    log.setStartTime(startTime);
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                    throw new DataPollException("TASK FAILED: " + e.getMessage());
                }
            }


            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            String logStr = null;
            for (int i = 0; i < totalPages; i++) {
                String rawJsonData = "";
                Timestamp timestamp = null;
                try {
                    int startRow = i * batchSize + 1;
                    int endRow = (i + 1) * batchSize;

                    String encodedData = "";
                    if (i == 0) {
                        // First batch pull record will null time stamp
                        encodedData = iPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, String.valueOf(startRow), String.valueOf(endRow));
                    } else {
                        encodedData = iPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad,
                                timeStampForPoll, false, String.valueOf(startRow), String.valueOf(endRow));
                    }

                    rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                    timestamp = getCurrentTimestamp();
                } catch (Exception e) {
                    logStr = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, tableName, timestamp,
                        rawJsonData, isInitialLoad, logStr, source, keyList,
                        startTime);

            }
        } catch (Exception e) {
            logger.error("TASK failed. tableName: {}, message: {}", tableName, e.getMessage());
        }
    }

    protected String getPollTimestamp(boolean isInitialLoad, String tableName) {
        String timeStampForPoll;
        if (isInitialLoad) {
            timeStampForPoll = iPollCommonService.getCurrentTimestamp();
        } else {
            if(storeJsonInS3) {
                timeStampForPoll = iPollCommonService.getLastUpdatedTimeS3(tableName);
            }
            else if (storeInSql)
            {
                timeStampForPoll = iPollCommonService.getLastUpdatedTime(tableName);
            }
            else
            {
                timeStampForPoll = iPollCommonService.getLastUpdatedTimeLocalDir(tableName);
            }
        }
        return timeStampForPoll;
    }

    @SuppressWarnings("java:S107")
    protected void updateDataHelper(boolean exceptionAtApiLevel, String tableName, Timestamp timestamp,
                                    String rawJsonData, boolean isInitialLoad, String log, String source, String keyList,
                                    Timestamp startTime) {
        LogResponseModel logResponseModel = new LogResponseModel();

        try {
            if (exceptionAtApiLevel) {
                logResponseModel.setStatus(ERROR);
                logResponseModel.setStartTime(startTime);
                logResponseModel.setLog(API_LEVEL + log);

                if (storeInSql) {
                    iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, logResponseModel);
                }
                else if (storeJsonInS3)
                {
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, logResponseModel);
                }
                else
                {
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
                }
            } else {
                if (storeJsonInS3) {
                    logResponseModel = is3DataService.persistToS3MultiPart(source, rawJsonData, tableName, timestamp, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(S3_LOG + log);

                    iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, logResponseModel);
                }
                else if (storeInSql)
                {
                    logResponseModel = rdbModernDataPersistentDAO.saveRdbModernData(tableName, rawJsonData, keyList, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(SQL_LOG + log);

                    iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, logResponseModel);
                }
                else
                {
                    logResponseModel = iPollCommonService.writeJsonDataToFile(source, tableName, timestamp, rawJsonData);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(LOCAL_DIR_LOG + log);

                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
                }
            }
        } catch (Exception e) {
            logResponseModel = new LogResponseModel();
            logResponseModel.setStatus(ERROR);
            logResponseModel.setStartTime(startTime);
            logResponseModel.setLog(CRITICAL_NON_NULL_LOG + e.getMessage());
            logResponseModel.setStackTrace(getStackTraceAsString(e));
            iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbModernDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}