package gov.cdc.nnddatapollservice.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.share.DataSimplification;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RdbDataHandlingService implements IRdbDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(RdbDataHandlingService.class);
    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.endpoint_generic}")
    protected String exchangeEndpoint;

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ITokenService tokenService;
    private final RdbDataPersistentDAO rdbDataPersistentDAO;

    public RdbDataHandlingService(ITokenService tokenService,
                                  RdbDataPersistentDAO rdbDataPersistentDAO) {
        this.tokenService = tokenService;
        this.rdbDataPersistentDAO = rdbDataPersistentDAO;
    }

    public void handlingExchangedData() throws DataPollException {
        logger.info("---START RDB POLLING---");
        List<PollDataSyncConfig> configTableList = getTableListFromConfig();
        List<PollDataSyncConfig> rdbTablesList = getTablesConfigListBySOurceDB(configTableList,"RDB");
        logger.info(" RDB TableList to be polled: {}",rdbTablesList.size());
        List<PollDataSyncConfig> srteTablesList = getTablesConfigListBySOurceDB(configTableList,"SRTE");
        logger.info(" SRTE TableList to be polled: {}",srteTablesList.size());

        boolean isInitalLoad = checkPollingIsInitailLoad(configTableList);
        logger.info("-----INITIAL LOAD: {}",isInitalLoad);

//        if (isInitalLoad) {
//            logger.info("For INITIAL LOAD - CLEANING UP THE TABLES ");
//            cleanupRDBTables(configTableList);
//        }
//
//        for (PollDataSyncConfig pollDataSyncConfig : configTableList) {
//            logger.info("Start polling: Table:{} order:{}",pollDataSyncConfig.getTableName(),pollDataSyncConfig.getTableOrder());
//            pollAndPeristsRDBData(pollDataSyncConfig.getTableName(), isInitalLoad);
//        }
        logger.info("---END RDB POLLING---");
    }

    private void pollAndPeristsRDBData(String tableName, boolean isInitialLoad) throws DataPollException {
        var token = tokenService.getToken();
        logger.info("--START--pollAndPeristsRDBData for table {}", tableName);
        String timeStampForPoll = "";
        if (isInitialLoad) {
            timeStampForPoll = getCurrentTimestamp();
        } else {
            timeStampForPoll = rdbDataPersistentDAO.getLastUpdatedTime(tableName);
        }
        logger.info("isInitalLoad {}", isInitialLoad);

        logger.info("------lastUpdatedTime to send to exchange api {}", timeStampForPoll);
        //call data exchange service api
        String encodedData = callDataExchangeEndpoint(token, tableName, isInitialLoad, timeStampForPoll);

        String rawData = decodeAndDecompress(encodedData);

        Timestamp timestamp = Timestamp.from(Instant.now());
        persistRdbData(tableName, rawData);

        rdbDataPersistentDAO.updateLastUpdatedTime(tableName, timestamp);
    }

    protected String callDataExchangeEndpoint(String token, String tableName, boolean isInitialLoad, String lastUpdatedTime) throws DataPollException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            headers.add("initialLoad", String.valueOf(isInitialLoad));
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            URI uri = UriComponentsBuilder.fromHttpUrl(exchangeEndpoint)
                    .path("/" + tableName)
                    .queryParamIfPresent("timestamp", Optional.ofNullable(lastUpdatedTime))
                    .build()
                    .toUri();
            logger.info("Exchange URI for rdb polling {} ", uri);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }
    }

    public String decodeAndDecompress(String base64EncodedData) {
        return DataSimplification.decodeAndDecompress(base64EncodedData);
    }

    private void persistRdbData(String tableName, String jsonData) {
        rdbDataPersistentDAO.saveRDBData(tableName, jsonData);
    }

    private List<PollDataSyncConfig> getTableListFromConfig() {
        return rdbDataPersistentDAO.getTableListFromConfig();
    }

    private void cleanupRDBTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            rdbDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }

    private boolean checkPollingIsInitailLoad(List<PollDataSyncConfig> configTableList) {
        for (PollDataSyncConfig pollDataSyncConfig : configTableList) {
            logger.info("pollDataSyncConfig Table:{}  LastUpdateTime:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getLastUpdateTime());
            if (pollDataSyncConfig.getLastUpdateTime() != null && !pollDataSyncConfig.getLastUpdateTime().toString().isBlank()) {
                return false;
            }
        }
        return true;
    }

    private String getCurrentTimestamp() {
        Timestamp timestamp = Timestamp.from(Instant.now());
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
        return formatter.format(timestamp);
    }
    private List<PollDataSyncConfig> getTablesConfigListBySOurceDB(List<PollDataSyncConfig> configTableList, String sourceDB){
        List<PollDataSyncConfig> tablesList=configTableList.stream().filter(configObj -> Objects.equals(configObj.getSourceDb(), sourceDB)).collect(Collectors.toList());
        return tablesList;
    }
}