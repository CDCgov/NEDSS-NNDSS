package gov.cdc.nnddatapollservice.universal.service;

import gov.cdc.nnddatapollservice.edx_nbs_odse.dao.EdxNbsOdseDataPersistentDAO;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.universal.dao.UniversalDataPersistentDAO;
import gov.cdc.nnddatapollservice.universal.service.interfaces.IUniversalDataHandlingService;
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

    private final UniversalDataPersistentDAO universalDataPersistentDAO;
    private final IPollCommonService iPollCommonService;
    private final IS3DataService is3DataService;
    private final EdxNbsOdseDataPersistentDAO edxNbsOdseDataPersistentDAO;

    public UniversalDataHandlingService(UniversalDataPersistentDAO universalDataPersistentDAO,
                                        IPollCommonService iPollCommonService,
                                        IS3DataService is3DataService,
                                        EdxNbsOdseDataPersistentDAO edxNbsOdseDataPersistentDAO) {
        this.universalDataPersistentDAO = universalDataPersistentDAO;
        this.iPollCommonService = iPollCommonService;
        this.is3DataService = is3DataService;
        this.edxNbsOdseDataPersistentDAO = edxNbsOdseDataPersistentDAO;
    }

    public void handlingExchangedData(String source) throws DataPollException {
        List<PollDataSyncConfig> configTableList = iPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> filteredTablesList = iPollCommonService.getTablesConfigListBySOurceDB(configTableList, source);

        boolean isInitialLoad = iPollCommonService.checkPollingIsInitailLoad(filteredTablesList);

        if (isInitialLoad && storeInSql && deleteOnInit) {
            cleanupTables(filteredTablesList);
        }

        List<PollDataSyncConfig> descList = filteredTablesList.stream()
                .sorted((a, b) -> Integer.compare(b.getTableOrder(), a.getTableOrder())) // Sorting in descending order
                .toList();

        for(PollDataSyncConfig pollDataSyncConfig : descList) {
            if (pollDataSyncConfig.isRecreateApplied() && storeInSql) {
                iPollCommonService.deleteTable(pollDataSyncConfig.getTableName());
            }
        }

        List<PollDataSyncConfig> ascList = filteredTablesList.stream()
                .sorted((a, b) -> Integer.compare(a.getTableOrder(), b.getTableOrder())) // Sort by tableOrder ASC
                .toList();


        for (PollDataSyncConfig pollDataSyncConfig : ascList) {
            pollAndPersistData(isInitialLoad, pollDataSyncConfig);
        }

    }

    @SuppressWarnings({"java:S1141","java:S3776"})
    protected void pollAndPersistData(boolean isInitialLoad, PollDataSyncConfig config)  {
        try {
            LogResponseModel log = null;
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts = 0;

            if(config.isRecreateApplied() ) {
                // IF recreated applied, EXPLICITLY set initialLoad to true, so the flow can be rerun
                isInitialLoad = true;
            }
            String timeStampForPoll = getPollTimestamp(isInitialLoad, config.getTableName());
            var timestampWithNull = getCurrentTimestamp();
            var startTime = getCurrentTimestamp();

            String logStr = "";


            try {
                totalRecordCounts = iPollCommonService.callDataCountEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                log = new LogResponseModel(CRITICAL_COUNT_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                iPollCommonService.updateLogNoTimestamp(config.getTableName(), log);
                throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
            }



            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            if (!config.isNoPagination()) {
                try {
                    var encodedDataWithNull = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, true,
                            "0", "0", false);
                    var rawJsonDataWithNull = iPollCommonService.decodeAndDecompress(encodedDataWithNull);
                    if (storeJsonInS3) {
                        log = is3DataService.persistToS3MultiPart(config.getSourceDb(), rawJsonDataWithNull, config.getTableName(), timestampWithNull, isInitialLoad);
                        log.setStartTime(startTime);
                        log.setLog(S3_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestampWithNull, log);
                    }
                    else if (storeInSql) {
                        if (config.getSourceDb().equalsIgnoreCase(NBS_ODSE_EDX)) {
                            log =  edxNbsOdseDataPersistentDAO.saveNbsOdseData(config.getTableName(), rawJsonDataWithNull);
                        }
                        else {
                            log =  universalDataPersistentDAO.saveRdbModernData(config, rawJsonDataWithNull, isInitialLoad);
                        }
                        log.setStartTime(startTime);
                        log.setLog(SQL_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestampWithNull, log);
                    }
                    else  {
                        log = iPollCommonService.writeJsonDataToFile(config.getSourceDb(), config.getTableName(), timestampWithNull, rawJsonDataWithNull);
                        log.setStartTime(startTime);
                        log.setLog(LOCAL_DIR_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestampWithNull, log);
                    }
                }
                catch (Exception e) {
                    log = new LogResponseModel(CRITICAL_NULL_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                    iPollCommonService.updateLogNoTimestamp(config.getTableName(), log);
                    throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
                }


                for (int i = 0; i < totalPages; i++) {
                    String rawJsonData = "";
                    Timestamp timestamp = null;
                    try {
                        int startRow = i * batchSize + 1;
                        int endRow = (i + 1) * batchSize;

                        String encodedData = "";
                        if (i == 0) {
                            // First batch pull record will null time stamp
                            encodedData = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, true,
                                    String.valueOf(startRow), String.valueOf(endRow), false);
                        } else {
                            encodedData = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, false,
                                    String.valueOf(startRow), String.valueOf(endRow), false);
                        }

                        rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                        timestamp = getCurrentTimestamp();
                    } catch (Exception e) {
                        logStr = e.getMessage();
                        exceptionAtApiLevel = true;
                    }

                    updateDataHelper(exceptionAtApiLevel, timestamp,
                            rawJsonData, isInitialLoad, logStr,
                            startTime, config);

                }
            }
            else
            {
                String rawJsonData = "";
                Timestamp timestamp = null;

                try {
                    String encodedData = "";
                    encodedData = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, false,
                            "0", "0", true);


                    rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                    timestamp = getCurrentTimestamp();
                } catch (Exception e) {
                    logStr = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, timestamp,
                        rawJsonData, isInitialLoad, logStr,
                        startTime, config);
            }




        } catch (Exception e) {
            logger.error("TASK failed. tableName: {}, message: {}", config.getTableName(), getStackTraceAsString(e));
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
    protected void updateDataHelper(boolean exceptionAtApiLevel, Timestamp timestamp,
                                    String rawJsonData, boolean isInitialLoad, String log,
                                    Timestamp startTime, PollDataSyncConfig config) {
        LogResponseModel logResponseModel = new LogResponseModel();

        try {
            if (exceptionAtApiLevel) {
                logResponseModel.setStatus(ERROR);
                logResponseModel.setStartTime(startTime);
                logResponseModel.setLog(API_LEVEL + log);

                if (storeInSql) {
                    iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestamp, logResponseModel);
                }
                else if (storeJsonInS3)
                {
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestamp, logResponseModel);
                }
                else
                {
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestamp, logResponseModel);
                }
            } else {
                if (storeJsonInS3) {
                    logResponseModel = is3DataService.persistToS3MultiPart(config.getSourceDb(), rawJsonData, config.getTableName(), timestamp, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(S3_LOG + log);
                    logResponseModel.setStatus(SUCCESS);
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestamp, logResponseModel);
                }
                else if (storeInSql)
                {
                    if (config.getSourceDb().equalsIgnoreCase(NBS_ODSE_EDX)) {
                        logResponseModel = edxNbsOdseDataPersistentDAO.saveNbsOdseData(config.getTableName(), rawJsonData);
                    }
                    else {
                        logResponseModel = universalDataPersistentDAO.saveRdbModernData(config, rawJsonData, isInitialLoad);

                    }
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(SQL_LOG + log);
                    logResponseModel.setStatus(SUCCESS);

                    iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestamp, logResponseModel);
                }
                else
                {
                    logResponseModel = iPollCommonService.writeJsonDataToFile(config.getSourceDb(), config.getTableName(), timestamp, rawJsonData);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(LOCAL_DIR_LOG + log);
                    logResponseModel.setStatus(SUCCESS);

                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestamp, logResponseModel);
                }
            }
        } catch (Exception e) {
            logResponseModel = new LogResponseModel();
            logResponseModel.setStatus(ERROR);
            logResponseModel.setStartTime(startTime);
            logResponseModel.setLog(CRITICAL_NON_NULL_LOG + e.getMessage());
            logResponseModel.setStackTrace(getStackTraceAsString(e));
            iPollCommonService.updateLogNoTimestamp(config.getTableName(), logResponseModel);
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            iPollCommonService.deleteTable(configTableList.get(j).getTableName());
        }
    }
}