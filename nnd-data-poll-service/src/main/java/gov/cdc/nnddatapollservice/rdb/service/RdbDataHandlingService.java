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
import java.util.stream.Collectors;

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


        List<PollDataSyncConfig> descList = rdbTablesList.stream()
                .sorted((a, b) -> Integer.compare(b.getTableOrder(), a.getTableOrder())) // Sorting in descending order
                .toList();

        for(PollDataSyncConfig pollDataSyncConfig : descList) {
            if (pollDataSyncConfig.isRecreateApplied() && storeInSql) {
                rdbDataPersistentDAO.deleteTable(pollDataSyncConfig.getTableName());
            }
        }

        List<PollDataSyncConfig> ascList = rdbTablesList.stream()
                .sorted((a, b) -> Integer.compare(a.getTableOrder(), b.getTableOrder())) // Sort by tableOrder ASC
                .toList();


        for (PollDataSyncConfig pollDataSyncConfig : ascList) {
            try {
                pollAndPersistRDBData(pollDataSyncConfig, isInitialLoad);
            } catch (Exception e){
                logger.error("Task error");
            }
        }
    }

    @SuppressWarnings("java:S1141")
    protected void pollAndPersistRDBData(PollDataSyncConfig pollConfig, boolean isInitialLoad) {
        try {
            LogResponseModel log = null;
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts;

            if(pollConfig.isRecreateApplied() ) {
                // IF recreated applied, EXPLICITLY set initialLoad to true, so the flow can be rerun
                isInitialLoad = true;
            }

            String timeStampForPoll = getPollTimestamp(isInitialLoad,  pollConfig.getTableName());

            var timestampWithNull = getCurrentTimestamp();
            Timestamp startTime = getCurrentTimestamp();

            String logStr = "";

            try {
                totalRecordCounts = pollCommonService.callDataCountEndpoint(pollConfig.getTableName(), isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                log = new LogResponseModel(CRITICAL_COUNT_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                pollCommonService.updateLastUpdatedTimeAndLogLocalDir(pollConfig.getTableName(), timestampWithNull, log);
                throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
            }
            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            if (!pollConfig.isNoPagination()) {
                try {
                    var encodedDataWithNull = pollCommonService.callDataExchangeEndpoint(pollConfig.getTableName(), isInitialLoad, timeStampForPoll,
                            true, "0", "0", false);
                    var rawJsonDataWithNull = pollCommonService.decodeAndDecompress(encodedDataWithNull);
                    if (storeJsonInS3) {
                        log = is3DataService.persistToS3MultiPart(RDB, rawJsonDataWithNull, pollConfig.getTableName(), timestampWithNull, isInitialLoad);
                        log.setStartTime(startTime);
                        log.setLog(S3_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        pollCommonService.updateLastUpdatedTimeAndLogS3(pollConfig.getTableName(), timestampWithNull, log);
                    }
                    else if (storeInSql) {
                        log =  rdbDataPersistentDAO.saveRDBData(pollConfig, rawJsonDataWithNull, isInitialLoad);
                        log.setStartTime(startTime);
                        log.setLog(SQL_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        pollCommonService.updateLastUpdatedTimeAndLog(pollConfig.getTableName(), timestampWithNull, log);
                    }
                    else  {
                        log = pollCommonService.writeJsonDataToFile(RDB, pollConfig.getTableName(), timestampWithNull, rawJsonDataWithNull);
                        log.setStartTime(startTime);
                        log.setLog(LOCAL_DIR_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        pollCommonService.updateLastUpdatedTimeAndLogLocalDir(pollConfig.getTableName(), timestampWithNull, log);
                    }
                }
                catch (Exception e) {
                    log = new LogResponseModel(CRITICAL_NULL_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(pollConfig.getTableName(), timestampWithNull, log);
                    throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
                }
                for (int i = 0; i < totalPages; i++) {
                    String rawJsonData = "";
                    Timestamp timestamp = null;

                    try {
                        int startRow = i * batchSize + 1;
                        int endRow = (i + 1) * batchSize;

                        String encodedData = "";
                        encodedData = pollCommonService.callDataExchangeEndpoint(pollConfig.getTableName(), isInitialLoad,
                                timeStampForPoll, false, String.valueOf(startRow), String.valueOf(endRow),
                                false);


                        rawJsonData = pollCommonService.decodeAndDecompress(encodedData);
                        timestamp = getCurrentTimestamp();
                    } catch (Exception e) {
                        logStr = e.getMessage();
                        exceptionAtApiLevel = true;
                    }

                    updateDataHelper(exceptionAtApiLevel, pollConfig, timestamp,
                            rawJsonData, isInitialLoad, logStr, startTime);

                }
            }
            else {
                String rawJsonData = "";
                Timestamp timestamp = null;

                try {
                    String encodedData = "";
                    encodedData = pollCommonService.callDataExchangeEndpoint(pollConfig.getTableName(), isInitialLoad, timeStampForPoll, false,
                            "0", "0", true);


                    rawJsonData = pollCommonService.decodeAndDecompress(encodedData);
                    timestamp = getCurrentTimestamp();
                } catch (Exception e) {
                    logStr = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, pollConfig, timestamp,
                        rawJsonData, isInitialLoad, logStr, startTime);
            }





        }catch (Exception e) {
            logger.error("TASK failed. tableName: {}, message: {}", pollConfig.getTableName(), e.getMessage());
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

    protected void updateDataHelper(boolean exceptionAtApiLevel, PollDataSyncConfig pollConfig, Timestamp timestamp,
                               String rawJsonData, boolean isInitialLoad, String log, Timestamp startTime) {

        LogResponseModel logResponseModel = new LogResponseModel();

        try {
            if (exceptionAtApiLevel)
            {
                logResponseModel.setStatus(ERROR);
                logResponseModel.setStartTime(startTime);
                logResponseModel.setLog(API_LEVEL + log);


                if (storeInSql) {
                    pollCommonService.updateLastUpdatedTimeAndLog(pollConfig.getTableName(), timestamp, logResponseModel);
                }
                else if (storeJsonInS3)
                {
                    pollCommonService.updateLastUpdatedTimeAndLogS3(pollConfig.getTableName(), timestamp, logResponseModel);
                }
                else
                {
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(pollConfig.getTableName(), timestamp, logResponseModel);
                }
            }
            else
            {
                if (storeJsonInS3) {
                    logResponseModel = is3DataService.persistToS3MultiPart(RDB, rawJsonData, pollConfig.getTableName(), timestamp, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(S3_LOG + log);
                    logResponseModel.setStatus(SUCCESS);
                    pollCommonService.updateLastUpdatedTimeAndLogS3(pollConfig.getTableName(), timestamp, logResponseModel);
                }
                else if (storeInSql) {
                    logResponseModel =  rdbDataPersistentDAO.saveRDBData(pollConfig, rawJsonData, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(SQL_LOG + log);
                    logResponseModel.setStatus(SUCCESS);
                    pollCommonService.updateLastUpdatedTimeAndLog(pollConfig.getTableName(), timestamp, logResponseModel);
                }
                else  {
                    logResponseModel = pollCommonService.writeJsonDataToFile(RDB, pollConfig.getTableName(), timestamp, rawJsonData);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(LOCAL_DIR_LOG + log);
                    logResponseModel.setStatus(SUCCESS);
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(pollConfig.getTableName(), timestamp, logResponseModel);
                }
            }
        } catch (Exception e) {
            logResponseModel = new LogResponseModel();
            logResponseModel.setStatus(ERROR);
            logResponseModel.setStartTime(startTime);
            logResponseModel.setLog(CRITICAL_NON_NULL_LOG + e.getMessage());
            logResponseModel.setStackTrace(getStackTraceAsString(e));
            pollCommonService.updateLastUpdatedTimeAndLogLocalDir(pollConfig.getTableName(), timestamp, logResponseModel);
        }
    }

    private void cleanupRDBTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}