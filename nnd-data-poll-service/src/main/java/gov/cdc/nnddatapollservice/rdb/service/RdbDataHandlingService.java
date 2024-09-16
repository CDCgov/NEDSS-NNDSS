package gov.cdc.nnddatapollservice.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class RdbDataHandlingService implements IRdbDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(RdbDataHandlingService.class);
    @Value("${datasync.store_in_local}")
    private boolean storeJsonInLocalFolder;
    @Value("${datasync.store_in_S3}")
    private boolean storeJsonInS3;

    private static final String RDB = "RDB";

    private final RdbDataPersistentDAO rdbDataPersistentDAO;
    private final IPollCommonService pollCommonService;

    public RdbDataHandlingService(
            RdbDataPersistentDAO rdbDataPersistentDAO,
            IPollCommonService outboundPollCommonService) {
        this.rdbDataPersistentDAO = rdbDataPersistentDAO;
        this.pollCommonService = outboundPollCommonService;
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

    private void pollAndPersistRDBData(String tableName, boolean isInitialLoad) throws DataPollException {
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

        String rawJsonData = pollCommonService.decodeAndDecompress(encodedData);

        Timestamp timestamp = Timestamp.from(Instant.now());
        rdbDataPersistentDAO.saveRDBData(tableName, rawJsonData);

        pollCommonService.updateLastUpdatedTime(tableName, timestamp);
        if(storeJsonInLocalFolder) {
            pollCommonService.writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData);
        }
        if(storeJsonInS3) {
            //STORE JSON FILES in S3 FOLDER
        }
    }

    private void cleanupRDBTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}