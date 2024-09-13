package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.service.RdbDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IOutboundPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.share.DataSimplification;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import gov.cdc.nnddatapollservice.srte.dao.SrteDataPersistentDAO;
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
public class OutboundPollCommonService implements IOutboundPollCommonService {
    private static Logger logger = LoggerFactory.getLogger(RdbDataHandlingService.class);
    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.endpoint_generic}")
    protected String exchangeEndpoint;

    @Value("${datasync.local_file_path}")
    private String datasyncLocalFilePath;

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ITokenService tokenService;
    private final RdbDataPersistentDAO rdbDataPersistentDAO;

    public OutboundPollCommonService(ITokenService tokenService,
                                     RdbDataPersistentDAO rdbDataPersistentDAO,
                                     SrteDataPersistentDAO srteDataPersistentDAO) {
        this.tokenService = tokenService;
        this.rdbDataPersistentDAO = rdbDataPersistentDAO;
    }

    public String callDataExchangeEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime) throws DataPollException {
        try {
            //Get token
            var token = tokenService.getToken();
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

    public List<PollDataSyncConfig> getTableListFromConfig() {
        return rdbDataPersistentDAO.getTableListFromConfig();
    }

    public boolean checkPollingIsInitailLoad(List<PollDataSyncConfig> configTableList) {
        for (PollDataSyncConfig pollDataSyncConfig : configTableList) {
            logger.info("pollDataSyncConfig Table:{}  LastUpdateTime:{}", pollDataSyncConfig.getTableName(), pollDataSyncConfig.getLastUpdateTime());
            if (pollDataSyncConfig.getLastUpdateTime() != null && !pollDataSyncConfig.getLastUpdateTime().toString().isBlank()) {
                return false;
            }
        }
        return true;
    }

    public String getCurrentTimestamp() {
        Timestamp timestamp = Timestamp.from(Instant.now());
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
        return formatter.format(timestamp);
    }
    public String getLastUpdatedTime(String tableName){
        return rdbDataPersistentDAO.getLastUpdatedTime(tableName);
    }
    public void updateLastUpdatedTime(String tableName, Timestamp timestamp){
        rdbDataPersistentDAO.updateLastUpdatedTime(tableName, timestamp);
    }
    public List<PollDataSyncConfig> getTablesConfigListBySOurceDB(List<PollDataSyncConfig> configTableList, String sourceDB) {
        List<PollDataSyncConfig> tablesList = configTableList.stream().filter(configObj -> Objects.equals(configObj.getSourceDb(), sourceDB)).collect(Collectors.toList());
        return tablesList;
    }

    public void writeJsonDataToFile(String dbSource, String tableName, Timestamp timeStamp, String jsonData) {
        PollServiceUtil.writeJsonToFile(datasyncLocalFilePath, dbSource, tableName, timeStamp, jsonData);
    }
    public String decodeAndDecompress(String base64EncodedData) {
        return DataSimplification.decodeAndDecompress(base64EncodedData);
    }
}