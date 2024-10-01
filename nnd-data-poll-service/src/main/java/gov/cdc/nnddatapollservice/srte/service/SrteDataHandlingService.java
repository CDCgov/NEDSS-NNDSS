package gov.cdc.nnddatapollservice.srte.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.srte.dao.SrteDataPersistentDAO;
import gov.cdc.nnddatapollservice.srte.service.interfaces.ISrteDataHandlingService;
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
public class SrteDataHandlingService implements ISrteDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(SrteDataHandlingService.class);
    @Value("${datasync.store_in_local}")
    protected boolean storeJsonInLocalFolder = false;
    @Value("${datasync.store_in_S3}")
    protected boolean storeJsonInS3 = false;
    @Value("${datasync.store_in_sql}")
    protected boolean storeInSql = false;
    @Value("${nnd.pullLimit}")
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
        logger.info("---START SRTE POLLING---");
        List<PollDataSyncConfig> configTableList = outboundPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> srteTablesList = outboundPollCommonService.getTablesConfigListBySOurceDB(configTableList, SRTE);
        logger.info("SRTE TableList to be polled: {}", srteTablesList.size());

        boolean isInitialLoad = outboundPollCommonService.checkPollingIsInitailLoad(srteTablesList);
        logger.info("-----SRTE INITIAL LOAD: {}", isInitialLoad);
        //Delete the existing records
        if (isInitialLoad && storeInSql && deleteOnInit) {
            cleanupTables(srteTablesList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : srteTablesList) {
            logger.info("Start polling: Table:{} order:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getTableOrder());
            pollAndPersistSRTEData(pollDataSyncConfig.getTableName(), isInitialLoad);
        }

        logger.info("---END SRTE POLLING---");
    }

    @SuppressWarnings("java:S1141")
    protected void pollAndPersistSRTEData(String tableName, boolean isInitialLoad) {
        try {
            logger.info("--START--pollAndPersistSRTEData for table {}", tableName);
            String log = "";
            boolean exceptionAtApiLevel = false;
            String timeStampForPoll = getPollTimestamp( isInitialLoad, tableName);
            Integer totalRecordCounts = 0;

            logger.info("------lastUpdatedTime to send to exchange api {}", timeStampForPoll);
            var timestampWithNull = Timestamp.from(Instant.now());

            //call data exchange service api
            try {
                totalRecordCounts = outboundPollCommonService.callDataCountEndpoint(tableName, isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_COUNT_LEVEL + e.getMessage());
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }
            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            try {
                var encodedDataWithNull = outboundPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, "0", "0");
                var rawJsonDataWithNull = outboundPollCommonService.decodeAndDecompress(encodedDataWithNull);
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonDataWithNull, tableName, timestampWithNull, isInitialLoad);
                    outboundPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestampWithNull, S3_LOG + log);
                }
                else if (storeInSql) {
                    log =  srteDataPersistentDAO.saveSRTEData(tableName, rawJsonDataWithNull);
                    outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestampWithNull, SQL_LOG + log);
                }
                else  {
                    log = outboundPollCommonService.writeJsonDataToFile(RDB, tableName, timestampWithNull, rawJsonDataWithNull);
                    outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, LOCAL_DIR_LOG + log);
                }
            } catch (Exception e) {
                outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_NULL_LEVEL + e.getMessage());
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }



            for (int i = 0; i < totalPages; i++) {
                String rawJsonData = "";
                Timestamp timestamp = null;

                try {
                    int startRow = i * batchSize + 1;
                    int endRow = (i + 1) * batchSize;

                    String encodedData = "";
                    encodedData = outboundPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, false, String.valueOf(startRow), String.valueOf(endRow));
                    rawJsonData = outboundPollCommonService.decodeAndDecompress(encodedData);
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
                                    String rawJsonData, boolean isInitialLoad, String log) {
        try {
            if (exceptionAtApiLevel)
            {
                if (storeInSql) {
                    outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, API_LEVEL + log);
                }
                else if (storeJsonInS3) {
                    outboundPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, API_LEVEL + log);
                }
                else {
                    outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, API_LEVEL + log);
                }
            }
            else
            {
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonData, tableName, timestamp, isInitialLoad);
                    outboundPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, S3_LOG + log);
                }
                else if (storeInSql) {
                    log =  srteDataPersistentDAO.saveSRTEData(tableName, rawJsonData);
                    outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + log);
                }
                else {
                    log = outboundPollCommonService.writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData);
                    outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, LOCAL_DIR_LOG + log);
                }
            }
        } catch (Exception e) {
            outboundPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, CRITICAL_NON_NULL_LEVEL + e.getMessage());
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            srteDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}