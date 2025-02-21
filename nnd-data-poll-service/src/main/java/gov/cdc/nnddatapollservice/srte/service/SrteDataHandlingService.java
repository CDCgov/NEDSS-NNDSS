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
    private final IPollCommonService iPollCommonService;
    private final IS3DataService is3DataService;

    public SrteDataHandlingService(
            SrteDataPersistentDAO srteDataPersistentDAO,
            IPollCommonService iPollCommonService,
            IS3DataService is3DataService) {
        this.srteDataPersistentDAO = srteDataPersistentDAO;
        this.iPollCommonService = iPollCommonService;
        this.is3DataService = is3DataService;
    }

    public void handlingExchangedData() throws DataPollException {
        List<PollDataSyncConfig> configTableList = iPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> srteTablesList = iPollCommonService.getTablesConfigListBySOurceDB(configTableList, SRTE);

        boolean isInitialLoad = iPollCommonService.checkPollingIsInitailLoad(srteTablesList);
        //Delete the existing records
        if (isInitialLoad && storeInSql && deleteOnInit) {
            cleanupTables(srteTablesList);
        }

        List<PollDataSyncConfig> descList = srteTablesList.stream()
                .sorted((a, b) -> Integer.compare(b.getTableOrder(), a.getTableOrder())) // Sorting in descending order
                .toList();

        for(PollDataSyncConfig pollDataSyncConfig : descList) {
            if (pollDataSyncConfig.isRecreateApplied() && storeInSql) {
                iPollCommonService.deleteTable(pollDataSyncConfig.getTableName());
            }
        }

        List<PollDataSyncConfig> ascList = srteTablesList.stream()
                .sorted((a, b) -> Integer.compare(a.getTableOrder(), b.getTableOrder())) // Sort by tableOrder ASC
                .toList();



        for (PollDataSyncConfig pollDataSyncConfig : ascList) {
            pollAndPersistSRTEData(isInitialLoad, pollDataSyncConfig);
        }

    }

    @SuppressWarnings("java:S1141")
    protected void pollAndPersistSRTEData(boolean isInitialLoad, PollDataSyncConfig config) {
        try {
            LogResponseModel log = null;
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts = 0;

            if(config.isRecreateApplied() ) {
                // IF recreated applied, EXPLICITLY set initialLoad to true, so the flow can be rerun
                isInitialLoad = true;
            }

            String timeStampForPoll = getPollTimestamp( isInitialLoad, config.getTableName());


            var timestampWithNull = TimestampUtil.getCurrentTimestamp();
            var startTime = getCurrentTimestamp();

            //call data exchange service api
            try {
                totalRecordCounts = iPollCommonService.callDataCountEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                log = new LogResponseModel(CRITICAL_COUNT_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestampWithNull, log);
                throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
            }

            String logStr = null;

            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            if (!config.isNoPagination())
            {
                try {
                    var encodedDataWithNull = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, true,
                            "0", "0", false);
                    var rawJsonDataWithNull = iPollCommonService.decodeAndDecompress(encodedDataWithNull);
                    if (storeJsonInS3) {
                        log = is3DataService.persistToS3MultiPart(RDB, rawJsonDataWithNull, config.getTableName(), timestampWithNull, isInitialLoad);
                        log.setStartTime(startTime);
                        log.setLog(S3_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestampWithNull, log);
                    }
                    else if (storeInSql) {
                        log = srteDataPersistentDAO.saveSRTEData(config, rawJsonDataWithNull, isInitialLoad);
                        log.setStartTime(startTime);
                        log.setLog(SQL_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestampWithNull, log);
                    }
                    else  {
                        log = iPollCommonService.writeJsonDataToFile(RDB, config.getTableName(), timestampWithNull, rawJsonDataWithNull);
                        log.setStartTime(startTime);
                        log.setLog(LOCAL_DIR_LOG + log.getLog());
                        log.setStatus(SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestampWithNull, log);
                    }
                }
                catch (Exception e)
                {
                    log =  new LogResponseModel();
                    log.setStatus(ERROR);
                    log.setLog(CRITICAL_NULL_LOG + e.getMessage());
                    log.setStackTrace(getStackTraceAsString(e));
                    log.setStartTime(startTime);
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestampWithNull, log);
                    throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
                }

                for (int i = 0; i < totalPages; i++) {
                    String rawJsonData = "";
                    Timestamp timestamp = null;

                    try {
                        int startRow = i * batchSize + 1;
                        int endRow = (i + 1) * batchSize;

                        String encodedData = "";
                        encodedData = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, false,
                                String.valueOf(startRow), String.valueOf(endRow), false);
                        rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                        timestamp = TimestampUtil.getCurrentTimestamp();
                    } catch (Exception e) {
                        logStr = e.getMessage();
                        exceptionAtApiLevel = true;
                    }

                    updateDataHelper(exceptionAtApiLevel, timestamp,
                            rawJsonData, isInitialLoad, logStr, startTime, config);

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
                        rawJsonData, isInitialLoad, logStr, startTime, config);
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
            if (storeInSql) {
                timeStampForPoll = iPollCommonService.getLastUpdatedTime(tableName);
            }
            else if (storeJsonInS3) {
                timeStampForPoll = iPollCommonService.getLastUpdatedTimeS3(tableName);
            }
            else {
                timeStampForPoll = iPollCommonService.getLastUpdatedTimeLocalDir(tableName);
            }
        }
        return timeStampForPoll;
    }

    protected void updateDataHelper(boolean exceptionAtApiLevel, Timestamp timestamp,
                                    String rawJsonData, boolean isInitialLoad, String log, Timestamp startTime,
                                    PollDataSyncConfig config) {
        LogResponseModel logResponseModel = new LogResponseModel();

        try {
            if (exceptionAtApiLevel)
            {
                logResponseModel.setStatus(ERROR);
                logResponseModel.setStartTime(startTime);
                logResponseModel.setLog(API_LEVEL + log);

                if (storeInSql) {
                    iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestamp, logResponseModel);
                }
                else if (storeJsonInS3) {
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestamp,  logResponseModel);
                }
                else {
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestamp, logResponseModel);
                }
            }
            else
            {
                if (storeJsonInS3) {
                    logResponseModel = is3DataService.persistToS3MultiPart(RDB, rawJsonData, config.getTableName(), timestamp, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(S3_LOG + log);
                    logResponseModel.setStatus(SUCCESS);
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestamp, logResponseModel);
                }
                else if (storeInSql) {
                    logResponseModel =  srteDataPersistentDAO.saveSRTEData(config, rawJsonData, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(SQL_LOG + log);
                    logResponseModel.setStatus(SUCCESS);
                    iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestamp, logResponseModel);
                }
                else {
                    logResponseModel = iPollCommonService.writeJsonDataToFile(RDB, config.getTableName(), timestamp, rawJsonData);
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
            iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestamp, logResponseModel);
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            iPollCommonService.deleteTable(configTableList.get(j).getTableName());
        }
    }
}