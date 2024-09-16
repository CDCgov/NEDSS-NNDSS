package gov.cdc.nnddatapollservice.srte.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
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

@Service
@Slf4j
public class SrteDataHandlingService implements ISrteDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(SrteDataHandlingService.class);
    @Value("${datasync.store_in_local}")
    private boolean storeJsonInLocalFolder;
    @Value("${datasync.store_in_S3}")
    private boolean storeJsonInS3;

    private static final String SRTE = "SRTE";

    private final SrteDataPersistentDAO srteDataPersistentDAO;
    private final IPollCommonService outboundPollCommonService;

    public SrteDataHandlingService(
            SrteDataPersistentDAO srteDataPersistentDAO,
            IPollCommonService outboundPollCommonService) {
        this.srteDataPersistentDAO = srteDataPersistentDAO;
        this.outboundPollCommonService = outboundPollCommonService;
    }

    public void handlingExchangedData() throws DataPollException {
        logger.info("---START SRTE POLLING---");
        List<PollDataSyncConfig> configTableList = outboundPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> srteTablesList = outboundPollCommonService.getTablesConfigListBySOurceDB(configTableList, SRTE);
        logger.info("SRTE TableList to be polled: {}", srteTablesList.size());

        boolean isInitialLoad = outboundPollCommonService.checkPollingIsInitailLoad(configTableList);
        logger.info("-----SRTE INITIAL LOAD: {}", isInitialLoad);
        //Delete the existing records
        cleanupTables(configTableList);

        for (PollDataSyncConfig pollDataSyncConfig : srteTablesList) {
            logger.info("Start polling: Table:{} order:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getTableOrder());
            pollAndPersistSRTEData(pollDataSyncConfig.getTableName(), isInitialLoad);
        }

        logger.info("---END SRTE POLLING---");
    }

    private void pollAndPersistSRTEData(String tableName, boolean isInitialLoad) throws DataPollException {
        logger.info("--START--pollAndPersistSRTEData for table {}", tableName);
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
        srteDataPersistentDAO.saveSRTEData(tableName, rawJsonData);

        outboundPollCommonService.updateLastUpdatedTime(tableName, timestamp);

        if (storeJsonInLocalFolder) {
            outboundPollCommonService.writeJsonDataToFile(SRTE, tableName, timestamp, rawJsonData);
        }
        if (storeJsonInS3) {
            //STORE JSON FILES in S3 FOLDER
        }
    }

    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            srteDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}