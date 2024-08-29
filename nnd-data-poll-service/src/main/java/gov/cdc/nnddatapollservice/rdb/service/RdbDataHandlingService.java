package gov.cdc.nnddatapollservice.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import jakarta.annotation.PostConstruct;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Service
@Slf4j
public class RdbDataHandlingService implements IRdbDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(RdbDataHandlingService.class);
    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.endpoint_generic}")
    private String exchangeEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ITokenService tokenService;
    private final RdbDataPersistentDAO rdbDataPersistentDAO;

    public RdbDataHandlingService(ITokenService tokenService,
                                  RdbDataPersistentDAO rdbDataPersistentDAO) {
        this.tokenService = tokenService;
        this.rdbDataPersistentDAO = rdbDataPersistentDAO;
    }

    @PostConstruct
    public void handlingExchangedData() throws DataPollException {

        logger.info("---START RDB POLLING---");
        List<PollDataSyncConfig> configTableList= getTableListFromConfig();
        logger.info(" RDB TableList to be polled: " + configTableList.size());
        boolean isInitalLoad= checkPollingIsInitailLoad(configTableList);
        System.out.println("isInitalLoad: " + isInitalLoad);
        if (isInitalLoad) {
            cleanupRDBTables(configTableList);
        }

        for (PollDataSyncConfig pollDataSyncConfig : configTableList) {
            logger.info("pollDataSyncConfig: order:"+pollDataSyncConfig.getTableOrder() +"  Table:"+ pollDataSyncConfig.getTableName());
            if(!pollDataSyncConfig.getTableName().equals("LDF_GROUP")
                    && !pollDataSyncConfig.getTableName().equals("HEP_MULTI_VALUE_FIELD_GROUP")
                    && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_SUSPECTED_SOURCE_GRP")
                    && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_TREATMENT_GROUP")
                    //&& !pollDataSyncConfig.getTableName().equals("CASE_COUNT")
                    && !pollDataSyncConfig.getTableName().equals("GENERIC_CASE")
                    && !pollDataSyncConfig.getTableName().equals("HEPATITIS_CASE")
                    && !pollDataSyncConfig.getTableName().equals("MEASLES_CASE")
                    && !pollDataSyncConfig.getTableName().equals("NOTIFICATION_EVENT")
                    && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_CASE")
                    && !pollDataSyncConfig.getTableName().equals("RUBELLA_CASE")) {
                pollAndPeristsRDBData(pollDataSyncConfig.getTableName(),isInitalLoad);
            }
        }
        logger.info("---END RDB POLLING---");
    }
    private void pollAndPeristsRDBData(String tableName,boolean isInitalLoad) throws DataPollException {
        var token = tokenService.getToken();
        var param = new HashMap<String, String>();
        logger.info("--START--handlingExchangedData for table " + tableName);
        String lastUpdatedTime= rdbDataPersistentDAO.getLastUpdatedTime(tableName);
        System.out.println("------lastUpdatedTime:"+lastUpdatedTime);
        //call data exchange service api
        String encodedData = callDataExchangeEndpoint(token, param, tableName,"");
        //logger.info("encoded compressed data from exchange for rdb: " + encodedData);
        String rawData = decodeAndDecompress(encodedData);
        //logger.info("raw data from exchange for the table: "+tableName+" " + rawData);
        Timestamp timestamp = Timestamp.from(Instant.now());
        persistRdbData(tableName, rawData);

        rdbDataPersistentDAO.updateLastUpdatedTime(tableName,timestamp);
        logger.info("--END--handlingExchangedData for table " + tableName);
    }

    protected String callDataExchangeEndpoint(String token, Map<String, String> params, String tableName,String lastUpdatedTime) throws DataPollException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String exchangeGenericEndpoint = exchangeEndpoint;

            URI uri = UriComponentsBuilder.fromHttpUrl(exchangeGenericEndpoint)
                    .path("/"+tableName)
                    .queryParamIfPresent("timestamp",Optional.ofNullable(lastUpdatedTime))
                    .build()
                    .toUri();
            logger.info("Poll service URI: " + uri);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }
    }

    public String decodeAndDecompress(String base64EncodedData) {
        byte[] compressedData = Base64.getDecoder().decode(base64EncodedData);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            String decompressedJson = byteArrayOutputStream.toString("UTF-8");

            return decompressedJson;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void persistRdbData(String tableName, String jsonData) throws DataPollException {
        rdbDataPersistentDAO.saveRDBData(tableName, jsonData);
    }
    private List<PollDataSyncConfig> getTableListFromConfig() throws DataPollException {
        return rdbDataPersistentDAO.getTableListFromConfig();
    }
    private void cleanupRDBTables(List<PollDataSyncConfig> configTableList) throws DataPollException {
        for(int j=configTableList.size()-1;j>=0;j=j-1){
            rdbDataPersistentDAO.deleteTable(configTableList.get(j).getTableName());
        }
    }
    private boolean checkPollingIsInitailLoad(List<PollDataSyncConfig> configTableList){
        for (PollDataSyncConfig pollDataSyncConfig : configTableList) {
            logger.info("pollDataSyncConfig Table:" + pollDataSyncConfig.getTableName() + "  LastUpdateTime:" + pollDataSyncConfig.getLastUpdateTime());
            if (pollDataSyncConfig.getLastUpdateTime()!=null && !pollDataSyncConfig.getLastUpdateTime().toString().isBlank()) {
                return false;
            }
        }
        return true;
    }
}