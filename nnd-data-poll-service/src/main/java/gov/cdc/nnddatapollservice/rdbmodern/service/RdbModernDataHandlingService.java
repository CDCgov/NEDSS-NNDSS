package gov.cdc.nnddatapollservice.rdbmodern.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdbmodern.dao.RdbModernDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdbmodern.service.interfaces.IRdbModernDataHandlingService;
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
public class RdbModernDataHandlingService implements IRdbModernDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(RdbModernDataHandlingService.class);
    @Value("${datasync.store_in_local}")
    private boolean storeJsonInLocalFolder;
    @Value("${datasync.store_in_S3}")
    private boolean storeJsonInS3;

    private static final String RDB_MODERN = "RDB_MODERN";

    private final RdbModernDataPersistentDAO rdbModernDataPersistentDAO;
    private final IPollCommonService outboundPollCommonService;

    public RdbModernDataHandlingService(RdbModernDataPersistentDAO rdbModernDataPersistentDAO,
                                        IPollCommonService outboundPollCommonService) {
        this.rdbModernDataPersistentDAO = rdbModernDataPersistentDAO;
        this.outboundPollCommonService = outboundPollCommonService;
    }

    public void handlingExchangedData() throws DataPollException {
        logger.info("---START RDB_MODERN POLLING---");
        List<PollDataSyncConfig> configTableList = outboundPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> rdbModernTablesList = outboundPollCommonService.getTablesConfigListBySOurceDB(configTableList, RDB_MODERN);
        logger.info(" RDB_MODERN TableList to be polled: {}", rdbModernTablesList.size());

        boolean isInitialLoad = outboundPollCommonService.checkPollingIsInitailLoad(rdbModernTablesList);
        logger.info("-----INITIAL LOAD: {}", isInitialLoad);

        if (isInitialLoad) {
            logger.info("For INITIAL LOAD - CLEANING UP THE RDB_MODERN TABLES ");
            cleanupTables(rdbModernTablesList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : rdbModernTablesList) {
            logger.info("Start polling: Table:{} order:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getTableOrder());
            pollAndPersistRDBMOdernData(pollDataSyncConfig.getTableName(), isInitialLoad);
        }

        logger.info("---END RDB_MODERN POLLING---");
    }

    private void pollAndPersistRDBMOdernData(String tableName, boolean isInitialLoad) throws DataPollException {
        logger.info("--START--pollAndPersistRDBData for table {}", tableName);
        String timeStampForPoll = "";
        if (isInitialLoad) {
            timeStampForPoll = outboundPollCommonService.getCurrentTimestamp();
        } else {
            timeStampForPoll = outboundPollCommonService.getLastUpdatedTime(tableName);
        }
        logger.info("isInitialLoad {}", isInitialLoad);

        logger.info("------lastUpdatedTime to send to exchange api {}", timeStampForPoll);
        //call data exchange service api
        String encodedData = outboundPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll);

        String rawJsonData = outboundPollCommonService.decodeAndDecompress(encodedData);

        Timestamp timestamp = Timestamp.from(Instant.now());
        rdbModernDataPersistentDAO.saveRdbModernData(tableName, rawJsonData);

        outboundPollCommonService.updateLastUpdatedTime(tableName, timestamp);

        if(storeJsonInLocalFolder) {
            outboundPollCommonService.writeJsonDataToFile(RDB_MODERN, tableName, timestamp, rawJsonData);
        }
        if(storeJsonInS3) {
            //STORE JSON FILES in S3 FOLDER
        }
    }

    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbModernDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}