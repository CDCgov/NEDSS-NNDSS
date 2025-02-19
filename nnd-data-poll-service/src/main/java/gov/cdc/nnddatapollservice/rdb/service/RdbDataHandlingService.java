package gov.cdc.nnddatapollservice.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
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
public class RdbDataHandlingService implements IRdbDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(RdbDataHandlingService.class);
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
    private final RdbDataPersistentDAO rdbDataPersistentDAO;
    private final IPollCommonService pollCommonService;
    private final IS3DataService is3DataService;

    public RdbDataHandlingService(
            RdbDataPersistentDAO rdbDataPersistentDAO,
            IPollCommonService pollCommonService,
            IS3DataService is3DataService) {
        this.rdbDataPersistentDAO = rdbDataPersistentDAO;
        this.pollCommonService = pollCommonService;
        this.is3DataService = is3DataService;
    }

    public void handlingExchangedData() throws DataPollException {
        List<PollDataSyncConfig> configTableList = pollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> rdbTablesList = pollCommonService.getTablesConfigListBySOurceDB(configTableList, RDB);

        boolean isInitialLoad = pollCommonService.checkPollingIsInitailLoad(rdbTablesList);
        logger.info("INITIAL LOAD: {}", isInitialLoad);

        if (isInitialLoad && storeInSql && deleteOnInit) {
            logger.info("CLEANING UP THE TABLES");
            cleanupRDBTables(rdbTablesList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : rdbTablesList) {
            try {
                pollAndPersistRDBData(pollDataSyncConfig.getTableName(), isInitialLoad);
            } catch (Exception e){
                logger.error("Task error");
            }
        }
    }

    @SuppressWarnings("java:S1141")
    protected void pollAndPersistRDBData(String tableName, boolean isInitialLoad) {
        try {
            LogResponseModel log = null;
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts;
            String timeStampForPoll = getPollTimestamp(isInitialLoad,  tableName);

            var timestampWithNull = getCurrentTimestamp();
            Timestamp startTime = getCurrentTimestamp();
            try {
                totalRecordCounts = pollCommonService.callDataCountEndpoint(tableName, isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                log = new LogResponseModel(CRITICAL_COUNT_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }
            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);
            try {
                var encodedDataWithNull = pollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, "0", "0");
                var rawJsonDataWithNull = pollCommonService.decodeAndDecompress(encodedDataWithNull);
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonDataWithNull, tableName, timestampWithNull, isInitialLoad);
                    log.setStartTime(startTime);
                    log.setLog(S3_LOG + log.getLog());
                    pollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestampWithNull, log);
                }
                else if (storeInSql) {
                    log =  rdbDataPersistentDAO.saveRDBData(tableName, rawJsonDataWithNull);
                    log.setStartTime(startTime);
                    log.setLog(SQL_LOG + log.getLog());
                    pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestampWithNull, log);
                }
                else  {
                    log = pollCommonService.writeJsonDataToFile(RDB, tableName, timestampWithNull, rawJsonDataWithNull);
                    log.setStartTime(startTime);
                    log.setLog(LOCAL_DIR_LOG + log.getLog());
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                }
            } catch (Exception e) {
                log = new LogResponseModel(CRITICAL_NULL_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, log);
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }

            String logStr = "";
            for (int i = 0; i < totalPages; i++) {
                String rawJsonData = "";
                Timestamp timestamp = null;

                try {
                    int startRow = i * batchSize + 1;
                    int endRow = (i + 1) * batchSize;

                    String encodedData = "";
                    encodedData = pollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, false, String.valueOf(startRow), String.valueOf(endRow));


                    rawJsonData = pollCommonService.decodeAndDecompress(encodedData);
                    timestamp = getCurrentTimestamp();
                } catch (Exception e) {
                    logStr = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, tableName, timestamp,
                        rawJsonData, isInitialLoad, logStr, startTime);

            }
        }catch (Exception e) {
            logger.error("TASK failed. tableName: {}, message: {}", tableName, e.getMessage());
        }

    }

    protected String getPollTimestamp(boolean isInitialLoad, String tableName) {
        String timeStampForPoll;
        if (isInitialLoad) {
            timeStampForPoll = pollCommonService.getCurrentTimestamp();
        } else {
            if (storeInSql) {
                timeStampForPoll = pollCommonService.getLastUpdatedTime(tableName);
            } else if (storeJsonInS3) {
                timeStampForPoll = pollCommonService.getLastUpdatedTimeS3(tableName);
            } else {
                timeStampForPoll = pollCommonService.getLastUpdatedTimeLocalDir(tableName);
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
                    pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, logResponseModel);
                }
                else if (storeJsonInS3)
                {
                    pollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, logResponseModel);
                }
                else
                {
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
                }
            }
            else
            {
                if (storeJsonInS3) {
                    logResponseModel = is3DataService.persistToS3MultiPart(RDB, rawJsonData, tableName, timestamp, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(S3_LOG + log);
                    pollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, logResponseModel);
                }
                else if (storeInSql) {
                    logResponseModel =  rdbDataPersistentDAO.saveRDBData(tableName, rawJsonData);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(SQL_LOG + log);
                    pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, logResponseModel);
                }
                else  {
                    logResponseModel = pollCommonService.writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(LOCAL_DIR_LOG + log);
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
                }
            }
        } catch (Exception e) {
            logResponseModel = new LogResponseModel();
            logResponseModel.setStatus(ERROR);
            logResponseModel.setStartTime(startTime);
            logResponseModel.setLog(CRITICAL_NON_NULL_LOG + e.getMessage());
            logResponseModel.setStackTrace(getStackTraceAsString(e));
            pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
        }
    }

    private void cleanupRDBTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}