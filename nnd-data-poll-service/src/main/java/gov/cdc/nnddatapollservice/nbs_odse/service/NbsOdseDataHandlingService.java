package gov.cdc.nnddatapollservice.nbs_odse.service;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.nbs_odse.dao.NbsOdseDataPersistentDAO;
import gov.cdc.nnddatapollservice.nbs_odse.service.interfaces.INbsOdseDataHandlingService;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.share.TimestampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;

@Service
public class NbsOdseDataHandlingService implements INbsOdseDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(NbsOdseDataHandlingService.class);
    private final IPollCommonService pollCommonService;
    private final NbsOdseDataPersistentDAO nbsOdseDataPersistentDAO;

    private final IS3DataService is3DataService;
    @Value("${datasync.store_in_local}")
    protected boolean storeJsonInLocalFolder = false;
    @Value("${datasync.store_in_S3}")
    protected boolean storeJsonInS3 = false;
    @Value("${datasync.store_in_sql}")
    protected boolean storeInSql = false;

    @Value("${datasync.data_sync_batch_limit}")
    protected Integer pullLimit = 0;

    private final Gson gson = new Gson();

    public NbsOdseDataHandlingService(IPollCommonService pollCommonService, NbsOdseDataPersistentDAO nbsOdseDataPersistentDAO, IS3DataService is3DataService) {
        this.pollCommonService = pollCommonService;
        this.nbsOdseDataPersistentDAO = nbsOdseDataPersistentDAO;
        this.is3DataService = is3DataService;
    }


    public void handlingExchangedData() throws DataPollException {
        logger.info("---START RDB_MODERN POLLING---");
        List<PollDataSyncConfig> configTableList = pollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> rdbModernTablesList = pollCommonService.getTablesConfigListBySOurceDB(configTableList, NBS_ODSE);
        logger.info(" RDB_MODERN TableList to be polled: {}", rdbModernTablesList.size());

        boolean isInitialLoad = pollCommonService.checkPollingIsInitailLoad(rdbModernTablesList);
        logger.info("-----INITIAL LOAD: {}", isInitialLoad);


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
            var timestampWithNull = TimestampUtil.getCurrentTimestamp();
            //call data exchange service api
            try {
                totalRecordCounts = pollCommonService.callDataCountEndpoint(tableName, isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_COUNT_LOG + log);
                throw new DataPollException("TASK FAILED: " + e.getMessage());
            }

            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            try {
                var encodedDataWithNull = pollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, "0", "0");
                var rawJsonDataWithNull = pollCommonService.decodeAndDecompress(encodedDataWithNull);
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(NBS_ODSE, rawJsonDataWithNull, tableName, timestampWithNull, isInitialLoad);
                    pollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestampWithNull, S3_LOG + log);
                }
                else if (storeInSql) {
                    log =  nbsOdseDataPersistentDAO.saveNbsOdseData(tableName, rawJsonDataWithNull);
                    pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestampWithNull, SQL_LOG + log);
                }
                else  {
                    log = pollCommonService.writeJsonDataToFile(NBS_ODSE, tableName, timestampWithNull, rawJsonDataWithNull);
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, LOCAL_DIR_LOG + log);
                }
            } catch (Exception e) {
                pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestampWithNull, CRITICAL_NULL_LOG + e.getMessage());
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
                        encodedData = pollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, true, String.valueOf(startRow), String.valueOf(endRow));
                    } else {
                        encodedData = pollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll, false, String.valueOf(startRow), String.valueOf(endRow));
                    }

                    rawJsonData = pollCommonService.decodeAndDecompress(encodedData);
                    timestamp = TimestampUtil.getCurrentTimestamp();
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
            timeStampForPoll = pollCommonService.getCurrentTimestamp();
        } else {
            if(storeJsonInS3) {
                timeStampForPoll = pollCommonService.getLastUpdatedTimeS3(tableName);
            }
            else if (storeInSql)
            {
                timeStampForPoll = pollCommonService.getLastUpdatedTime(tableName);
            }
            else
            {
                timeStampForPoll = pollCommonService.getLastUpdatedTimeLocalDir(tableName);
            }
        }
        return timeStampForPoll;
    }

    protected void updateDataHelper(boolean exceptionAtApiLevel, String tableName, Timestamp timestamp,
                                    String rawJsonData, boolean isInitialLoad, String log) {
        try {
            if (exceptionAtApiLevel) {
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
            } else {
                if (storeJsonInS3) {
                    log = is3DataService.persistToS3MultiPart(RDB, rawJsonData, tableName, timestamp, isInitialLoad);
                    pollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, S3_LOG + log);
                }
                else if (storeInSql)
                {
                    log = nbsOdseDataPersistentDAO.saveNbsOdseData(tableName, rawJsonData);
                    pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + log);
                }
                else
                {
                    log = pollCommonService.writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData);
                    pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, LOCAL_DIR_LOG + log);
                }
            }
        } catch (Exception e) {
            pollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, CRITICAL_NON_NULL_LOG + e.getMessage());
        }
    }


}
