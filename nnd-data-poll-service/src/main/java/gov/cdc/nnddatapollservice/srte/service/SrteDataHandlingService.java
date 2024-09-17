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
        cleanupTables(srteTablesList);

        for (PollDataSyncConfig pollDataSyncConfig : srteTablesList) {
            logger.info("Start polling: Table:{} order:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getTableOrder());
            pollAndPersistSRTEData(pollDataSyncConfig.getTableName(), isInitialLoad);
        }

        logger.info("---END SRTE POLLING---");
    }

    protected void pollAndPersistSRTEData(String tableName, boolean isInitialLoad) throws DataPollException {
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

        if(storeJsonInS3) {
            String log = is3DataService.persistToS3MultiPart(SRTE, rawJsonData, tableName, timestamp, isInitialLoad);
            outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, S3_LOG + log);
        }

        if (storeInSql) {
            String log = srteDataPersistentDAO.saveSRTEData(tableName, rawJsonData);
            outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + log);
        }

        if(storeJsonInLocalFolder) {
            outboundPollCommonService.writeJsonDataToFile(SRTE, tableName, timestamp, rawJsonData, isInitialLoad);
            outboundPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, LOCAL_DIR_LOG + log);
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            srteDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
}