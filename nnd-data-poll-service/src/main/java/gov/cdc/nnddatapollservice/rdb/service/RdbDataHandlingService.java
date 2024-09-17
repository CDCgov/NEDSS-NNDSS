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

    private final RdbDataPersistentDAO rdbDataPersistentDAO;
    private final IPollCommonService pollCommonService;

    private final IS3DataService is3DataService;

    public RdbDataHandlingService(
            RdbDataPersistentDAO rdbDataPersistentDAO,
            IPollCommonService outboundPollCommonService,
            IS3DataService is3DataService) {
        this.rdbDataPersistentDAO = rdbDataPersistentDAO;
        this.pollCommonService = outboundPollCommonService;
        this.is3DataService = is3DataService;
    }

    public void handlingExchangedData() throws DataPollException {
        logger.info("---START RDB POLLING---");
        List<PollDataSyncConfig> configTableList = pollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> rdbTablesList = pollCommonService.getTablesConfigListBySOurceDB(configTableList, RDB);
        logger.info(" RDB TableList to be polled: {}", rdbTablesList.size());

        boolean isInitialLoad = pollCommonService.checkPollingIsInitailLoad(configTableList);
        logger.info("-----INITIAL LOAD: {}", isInitialLoad);

        if (isInitialLoad) {
            logger.info("For INITIAL LOAD - CLEANING UP THE TABLES ");
            cleanupRDBTables(configTableList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : rdbTablesList) {
            logger.info("Start polling: Table:{} order:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getTableOrder());
            pollAndPersistRDBData(pollDataSyncConfig.getTableName(), isInitialLoad);
        }

        logger.info("---END RDB POLLING---");
    }

    protected void pollAndPersistRDBData(String tableName, boolean isInitialLoad) {
        String rawJsonData = null;
        Timestamp timestamp = null;
        String log = "";
        boolean exceptionAtApiLevel = false;
        try {
            logger.info("--START--pollAndPeristsRDBData for table {}", tableName);
            String timeStampForPoll = "";
            if (isInitialLoad) {
                timeStampForPoll = pollCommonService.getCurrentTimestamp();
            } else {
                timeStampForPoll = pollCommonService.getLastUpdatedTime(tableName);
            }
            logger.info("isInitialLoad {}", isInitialLoad);

            logger.info("------lastUpdatedTime to send to exchange api {}", timeStampForPoll);
            //call data exchange service api
            String encodedData = pollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll);
            rawJsonData = pollCommonService.decodeAndDecompress(encodedData);
            timestamp = Timestamp.from(Instant.now());
        } catch (Exception e) {
            log = e.getMessage();
            exceptionAtApiLevel = true;
        }


        if (exceptionAtApiLevel)
        {
            pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, API_LEVEL + log);
        }
        else
        {
            if (storeJsonInS3) {
                log = is3DataService.persistToS3MultiPart(RDB, rawJsonData, tableName, timestamp, isInitialLoad);
                pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, S3_LOG + log);
            }

            if (storeInSql) {
                log =  rdbDataPersistentDAO.saveRDBData(tableName, rawJsonData);
                pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + log);
            }

            if (storeJsonInLocalFolder) {
                log = pollCommonService.writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData, isInitialLoad);
                pollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, LOCAL_DIR_LOG + log);
            }
        }


    }

    private void cleanupRDBTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}