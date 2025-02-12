package gov.cdc.nnddatapollservice.rdbmodern.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdbmodern.dao.RdbModernDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdbmodern.service.interfaces.IUniversalDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.share.TimestampUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;

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
        logger.info("---START POLLING---");
        List<PollDataSyncConfig> configTableList = iPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> filteredTablesList = iPollCommonService.getTablesConfigListBySOurceDB(configTableList, source);
        logger.info("TableList to be polled: {}", filteredTablesList.size());

        boolean isInitialLoad = iPollCommonService.checkPollingIsInitailLoad(filteredTablesList);
        logger.info("-----INITIAL LOAD: {}", isInitialLoad);

        if (isInitialLoad && storeInSql && deleteOnInit) {
            logger.info("For INITIAL LOAD - CLEANING UP THE TABLES ");
            cleanupTables(filteredTablesList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : filteredTablesList) {
            logger.info("Start polling: Table:{} order:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getTableOrder());
            pollAndPersistRDBMOdernData(source, pollDataSyncConfig.getTableName(), isInitialLoad,
                    pollDataSyncConfig.getKeyList(), pollDataSyncConfig.isRecreateApplied());
        }

        logger.info("---END POLLING---");
    }

    @SuppressWarnings("java:S1141")
    protected void pollAndPersistRDBMOdernData(String source, String tableName, boolean isInitialLoad, String keyList,
                                               boolean recreatedApplied) {
        try {
            if (recreatedApplied && storeInSql) {
                rdbModernDataPersistentDAO.deleteTable(tableName);
            }
            String log = "";
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts = 0;
            String timeStampForPoll = getPollTimestamp(isInitialLoad, tableName);

            var timestampWithNull = TimestampUtil.getCurrentTimestamp();
            try {
                totalRecordCounts = iPollCommonService.callDataCountEndpoint(tableName, isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_COUNT_LOG + log);
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }


            if (!recreatedApplied) {
                try {
                    var encodedDataWithNull = iPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, "0", "0");
                    var rawJsonDataWithNull = iPollCommonService.decodeAndDecompress(encodedDataWithNull);
                    if (storeJsonInS3) {
                        log = is3DataService.persistToS3MultiPart(source, rawJsonDataWithNull, tableName, timestampWithNull, isInitialLoad);
                        iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestampWithNull, S3_LOG + log);
                    }
                    else if (storeInSql) {
                        log =  rdbModernDataPersistentDAO.saveRdbModernData(tableName, rawJsonDataWithNull, keyList,
                                isInitialLoad);
                        iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestampWithNull, SQL_LOG + log);
                    }
                    else  {
                        log = iPollCommonService.writeJsonDataToFile(source, tableName, timestampWithNull, rawJsonDataWithNull);
                        iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, LOCAL_DIR_LOG + log);
                    }
                }
                catch (Exception e) {
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_NULL_LOG + e.getMessage());
                    throw new DataPollException("TASK FAILED: " + e.getMessage());
                }
            }


            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

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
                    timestamp = TimestampUtil.getCurrentTimestamp();
                } catch (Exception e) {
                    log = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, tableName, timestamp,
                        rawJsonData, isInitialLoad, log, source, keyList);

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

    protected void updateDataHelper(boolean exceptionAtApiLevel, String tableName, Timestamp timestamp,
                                    String rawJsonData, boolean isInitialLoad, String log, String source, String keyList) {
        try {
            if (exceptionAtApiLevel) {
                if (storeInSql) {
                    iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, API_LEVEL + log);
                }
                else if (storeJsonInS3)
                {
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, API_LEVEL + log);
                }
                else
                {
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, API_LEVEL + log);
                }
            } else {
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(source, rawJsonData, tableName, timestamp, isInitialLoad);
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, S3_LOG + log);
                }
                else if (storeInSql)
                {
                    log = rdbModernDataPersistentDAO.saveRdbModernData(tableName, rawJsonData, keyList, isInitialLoad);
                    iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + log);
                }
                else
                {
                    log = iPollCommonService.writeJsonDataToFile(source, tableName, timestamp, rawJsonData);
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, LOCAL_DIR_LOG + log);
                }
            }
        } catch (Exception e) {
            iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, CRITICAL_NON_NULL_LOG + e.getMessage());
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbModernDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}