package gov.cdc.nnddatapollservice.rdbmodern.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdbmodern.dao.RdbModernDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdbmodern.service.interfaces.IRdbModernDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;

@Service
@Slf4j
public class RdbModernDataHandlingService implements IRdbModernDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(RdbModernDataHandlingService.class);
    @Value("${datasync.store_in_local}")
    protected boolean storeJsonInLocalFolder = false;
    @Value("${datasync.store_in_S3}")
    protected boolean storeJsonInS3 = false;
    @Value("${datasync.store_in_sql}")
    protected boolean storeInSql = false;
    @Value("${nnd.pullLimit}")
    protected Integer pullLimit = 0;
    private final RdbModernDataPersistentDAO rdbModernDataPersistentDAO;
    private final IPollCommonService iPollCommonService;
    private final IS3DataService is3DataService;

    public RdbModernDataHandlingService(RdbModernDataPersistentDAO rdbModernDataPersistentDAO,
                                        IPollCommonService iPollCommonService,
                                        IS3DataService is3DataService) {
        this.rdbModernDataPersistentDAO = rdbModernDataPersistentDAO;
        this.iPollCommonService = iPollCommonService;
        this.is3DataService = is3DataService;
    }

    public void handlingExchangedData() throws DataPollException {
        logger.info("---START RDB_MODERN POLLING---");
        List<PollDataSyncConfig> configTableList = iPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> rdbModernTablesList = iPollCommonService.getTablesConfigListBySOurceDB(configTableList, RDB_MODERN);
        logger.info(" RDB_MODERN TableList to be polled: {}", rdbModernTablesList.size());

        boolean isInitialLoad = iPollCommonService.checkPollingIsInitailLoad(rdbModernTablesList);
        logger.info("-----INITIAL LOAD: {}", isInitialLoad);

        if (isInitialLoad && storeInSql) {
            logger.info("For INITIAL LOAD - CLEANING UP THE RDB_MODERN TABLES ");
            cleanupTables(rdbModernTablesList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : rdbModernTablesList) {
            logger.info("Start polling: Table:{} order:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getTableOrder());
            pollAndPersistRDBMOdernData(pollDataSyncConfig.getTableName(), isInitialLoad);
        }

        logger.info("---END RDB_MODERN POLLING---");
    }

    protected void pollAndPersistRDBMOdernData(String tableName, boolean isInitialLoad) {
        try {
            logger.info("--START--pollAndPersistRDBData for table {}", tableName);
            String log = "";
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts = 0;
            String timeStampForPoll = getPollTimestamp(isInitialLoad, tableName);
            logger.info("isInitialLoad {}", isInitialLoad);

            logger.info("------lastUpdatedTime to send to exchange api {}", timeStampForPoll);
            var timestampWithNull = Timestamp.from(Instant.now());
            //call data exchange service api
            try {
                totalRecordCounts = iPollCommonService.callDataCountEndpoint(tableName, isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_COUNT_LEVEL + log);
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }

            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            try {
                var encodedDataWithNull = iPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, "0", "0");
                var rawJsonDataWithNull = iPollCommonService.decodeAndDecompress(encodedDataWithNull);
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonDataWithNull, tableName, timestampWithNull, isInitialLoad);
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestampWithNull, S3_LOG + log);
                }
                else if (storeInSql) {
                    log =  rdbModernDataPersistentDAO.saveRdbModernData(tableName, rawJsonDataWithNull);
                    iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestampWithNull, SQL_LOG + log);
                }
                else  {
                    log = iPollCommonService.writeJsonDataToFile(RDB, tableName, timestampWithNull, rawJsonDataWithNull);
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, LOCAL_DIR_LOG + log);
                }
            } catch (Exception e) {
                iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_NULL_LEVEL + e.getMessage());
                throw new DataPollException("TASK FAILED: " + e.getMessage());
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
                        encodedData = iPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, String.valueOf(startRow), String.valueOf(endRow));
                    } else {
                        encodedData = iPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, false, String.valueOf(startRow), String.valueOf(endRow));
                    }

                    rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                    timestamp = Timestamp.from(Instant.now());
                } catch (Exception e) {
                    log = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, tableName, timestamp,
                        rawJsonData, isInitialLoad, log);

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
                                    String rawJsonData, boolean isInitialLoad, String log) {
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
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonData, tableName, timestamp, isInitialLoad);
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, S3_LOG + log);
                }
                else if (storeInSql)
                {
                    log = rdbModernDataPersistentDAO.saveRdbModernData(tableName, rawJsonData);
                    iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + log);
                }
                else
                {
                    log = iPollCommonService.writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData);
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, LOCAL_DIR_LOG + log);
                }
            }
        } catch (Exception e) {
            iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, CRITICAL_NON_NULL_LEVEL + e.getMessage());
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbModernDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}