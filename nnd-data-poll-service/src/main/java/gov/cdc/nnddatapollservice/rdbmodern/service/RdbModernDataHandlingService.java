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

    protected void pollAndPersistRDBMOdernData(String tableName, boolean isInitialLoad) {
        logger.info("--START--pollAndPersistRDBData for table {}", tableName);
        String rawJsonData = null;
        Timestamp timestamp = null;
        String log = "";
        boolean exceptionAtApiLevel = false;
        try {
            String timeStampForPoll = "";
            if (isInitialLoad) {
                timeStampForPoll = iPollCommonService.getCurrentTimestamp();
            } else {
                timeStampForPoll = iPollCommonService.getLastUpdatedTime(tableName);
            }
            logger.info("isInitialLoad {}", isInitialLoad);

            logger.info("------lastUpdatedTime to send to exchange api {}", timeStampForPoll);
            //call data exchange service api
            String encodedData = iPollCommonService.callDataExchangeEndpoint(tableName, isInitialLoad, timeStampForPoll);

            rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
            timestamp = Timestamp.from(Instant.now());


        } catch (Exception e) {
            log = e.getMessage();
            exceptionAtApiLevel = true;
        }

        if (exceptionAtApiLevel)
        {
            iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, API_LEVEL + log);
        }
        else
        {
            if(storeJsonInS3) {
                log = is3DataService.persistToS3MultiPart(RDB_MODERN, rawJsonData, tableName, timestamp, isInitialLoad);
                iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, S3_LOG + log);
            }

            if (storeInSql) {
                log = rdbModernDataPersistentDAO.saveRdbModernData(tableName, rawJsonData);
                iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + log);
            }

            if(storeJsonInLocalFolder) {
                iPollCommonService.writeJsonDataToFile(RDB_MODERN, tableName, timestamp, rawJsonData, isInitialLoad);
                iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, LOCAL_DIR_LOG + log);
            }
        }



    }



    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbModernDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}