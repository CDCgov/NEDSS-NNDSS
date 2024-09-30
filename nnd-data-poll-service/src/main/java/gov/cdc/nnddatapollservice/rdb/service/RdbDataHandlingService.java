package gov.cdc.nnddatapollservice.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
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
public class RdbDataHandlingService implements IRdbDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(RdbDataHandlingService.class);
    @Value("${datasync.store_in_local}")
    protected boolean storeJsonInLocalFolder = false;
    @Value("${datasync.store_in_S3}")
    protected boolean storeJsonInS3 = false;
    @Value("${datasync.store_in_sql}")
    protected boolean storeInSql = false;
    @Value("${nnd.pullLimit}")
    protected Integer pullLimit = 0;

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
        logger.info("---START RDB POLLING---");
        List<PollDataSyncConfig> configTableList = pollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> rdbTablesList = pollCommonService.getTablesConfigListBySOurceDB(configTableList, RDB);
        logger.info(" RDB TableList to be polled: {}", rdbTablesList.size());

        boolean isInitialLoad = pollCommonService.checkPollingIsInitailLoad(rdbTablesList);
        logger.info("-----INITIAL LOAD: {}", isInitialLoad);

        if (isInitialLoad && storeInSql) {
            logger.info("For INITIAL LOAD - CLEANING UP THE TABLES ");
            cleanupRDBTables(rdbTablesList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : rdbTablesList) {
            logger.info("Start polling: Table:{} order:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getTableOrder());
            try {
                pollAndPersistRDBData(pollDataSyncConfig.getTableName(), isInitialLoad);
            } catch (Exception e){
                logger.info("Task error");
            }
        }

        logger.info("---END RDB POLLING---");
    }

    protected void pollAndPersistRDBData(String tableName, boolean isInitialLoad) {
        try {
            String log = "";
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts;
            logger.info("--START--pollAndPeristsRDBData for table {}", tableName);
            String timeStampForPoll = getPollTimestamp(isInitialLoad,  tableName);

            logger.info("isInitialLoad {}", isInitialLoad);

            logger.info("------lastUpdatedTime to send to exchange api {}", timeStampForPoll);
            var timestampWithNull = Timestamp.from(Instant.now());

            try {
                totalRecordCounts = pollCommonService.callDataCountEndpoint(tableName, isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_COUNT_LEVEL + e.getMessage());
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }
            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);
            try {
                var encodedDataWithNull = pollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, "0", "0");
                var rawJsonDataWithNull = pollCommonService.decodeAndDecompress(encodedDataWithNull);
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonDataWithNull, tableName, timestampWithNull, isInitialLoad);
                    pollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestampWithNull, S3_LOG + log);
                }
                else if (storeInSql) {
                    log =  rdbDataPersistentDAO.saveRDBData(tableName, rawJsonDataWithNull);
                    pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestampWithNull, SQL_LOG + log);
                }
                else  {
                    log = pollCommonService.writeJsonDataToFile(RDB, tableName, timestampWithNull, rawJsonDataWithNull);
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, LOCAL_DIR_LOG + log);
                }
            } catch (Exception e) {
                pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_NULL_LEVEL + e.getMessage());
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }


            for (int i = 0; i < totalPages; i++) {
                String rawJsonData = "";
                Timestamp timestamp = null;

                try {
                    int startRow = i * batchSize + 1;
                    int endRow = (i + 1) * batchSize;

                    String encodedData = "";
                    encodedData = pollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, false, String.valueOf(startRow), String.valueOf(endRow));


                    rawJsonData = pollCommonService.decodeAndDecompress(encodedData);
                    timestamp = Timestamp.from(Instant.now());
                } catch (Exception e) {
                    log = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, tableName, timestamp,
                        rawJsonData, isInitialLoad, log);

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
                               String rawJsonData, boolean isInitialLoad, String log) {
        try {
            if (exceptionAtApiLevel)
            {
                if (storeInSql) {
                    pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, API_LEVEL + log);
                }
                else if (storeJsonInS3)
                {
                    pollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, API_LEVEL + log);
                }
                else
                {
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, API_LEVEL + log);
                }
            }
            else
            {
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonData, tableName, timestamp, isInitialLoad);
                    pollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, S3_LOG + log);
                }
                else if (storeInSql) {
                    log =  rdbDataPersistentDAO.saveRDBData(tableName, rawJsonData);
                    pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + log);
                }
                else  {
                    log = pollCommonService.writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData);
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, LOCAL_DIR_LOG + log);
                }
            }
        } catch (Exception e) {
            pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, CRITICAL_NON_NULL_LEVEL + e.getMessage());
        }
    }

    private void cleanupRDBTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}