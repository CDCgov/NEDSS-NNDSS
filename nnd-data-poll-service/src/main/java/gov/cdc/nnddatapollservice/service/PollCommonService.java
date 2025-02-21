package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.DataSimplification;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import gov.cdc.nnddatapollservice.share.TimestampUtil;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class PollCommonService implements IPollCommonService {
    private final JdbcTemplateUtil jdbcTemplateUtil;

    private static Logger logger = LoggerFactory.getLogger(PollCommonService.class);
    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.endpoint_generic}")
    protected String exchangeEndpoint;

    @Value("${data_exchange.endpoint_generic_total_record}")
    protected String exchangeTotalRecordEndpoint;


    @Value("${datasync.local_file_path}")
    private String datasyncLocalFilePath;

    @Value("${datasync.store_in_sql}")
    private boolean sqlSync;

    @Value("${datasync.store_in_local}")
    private boolean dirSync;

    @Value("${datasync.store_in_S3}")
    private boolean s3Sync;


    @Value("${data_exchange.version}")
    private String version;

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ITokenService tokenService;
    private final RdbDataPersistentDAO rdbDataPersistentDAO;

    public PollCommonService(JdbcTemplateUtil jdbcTemplateUtil, ITokenService tokenService,
                             RdbDataPersistentDAO rdbDataPersistentDAO) {
        this.jdbcTemplateUtil = jdbcTemplateUtil;
        this.tokenService = tokenService;
        this.rdbDataPersistentDAO = rdbDataPersistentDAO;
    }

    public Integer callDataCountEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime) throws DataPollException {
        try {
            //Get token
            var token = tokenService.getToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            headers.add("initialLoad", String.valueOf(isInitialLoad));
            headers.add("version", version);
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            URI uri = UriComponentsBuilder.fromHttpUrl(exchangeTotalRecordEndpoint)
                    .path("/" + tableName)
                    .queryParamIfPresent("timestamp", Optional.ofNullable(lastUpdatedTime))
                    .build()
                    .toUri();
            logger.info("Exchange URI for rdb polling {} ", uri);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
            return Integer.valueOf(response.getBody());
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }
    }

    public String callDataExchangeEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime, boolean allowNull,
                                           String startRow, String endRow, boolean noPagination) throws DataPollException {
        try {
            //Get token
            var token = tokenService.getToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("initialLoad", String.valueOf(isInitialLoad));
            headers.add("allowNull", String.valueOf(allowNull));
            headers.add("startRow", startRow);
            headers.add("endRow",  endRow);
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            headers.add("version", version);
            headers.add("noPagination", String.valueOf(noPagination));
            headers.setBearerAuth(token);
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
        return jdbcTemplateUtil.getTableListFromConfig();
    }

    public boolean checkPollingIsInitailLoad(List<PollDataSyncConfig> configTableList) {
        if(dirSync) {
            for (PollDataSyncConfig pollDataSyncConfig : configTableList) {
                if (pollDataSyncConfig.getLastUpdateTimeLocalDir() != null
                        && !pollDataSyncConfig.getLastUpdateTimeLocalDir().toString().isBlank()) {
                    return false;
                }
            }
        } else if (s3Sync) {
            for (PollDataSyncConfig pollDataSyncConfig : configTableList) {
                if (pollDataSyncConfig.getLastUpdateTimeS3() != null
                        && !pollDataSyncConfig.getLastUpdateTimeS3().toString().isBlank()) {
                    return false;
                }
            }
        } else {
            for (PollDataSyncConfig pollDataSyncConfig : configTableList) {
                if (pollDataSyncConfig.getLastUpdateTime() != null && !pollDataSyncConfig.getLastUpdateTime().toString().isBlank()) {
                    return false;
                }
            }
        }


        return true;
    }

    public String getCurrentTimestamp() {
        Timestamp timestamp = TimestampUtil.getCurrentTimestamp();
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
        return formatter.format(timestamp);
    }

    public String getLastUpdatedTime(String tableName) {
        return jdbcTemplateUtil.getLastUpdatedTime(tableName);
    }

    public String getLastUpdatedTimeS3(String tableName) {
        return jdbcTemplateUtil.getLastUpdatedTimeS3(tableName);
    }

    public String getLastUpdatedTimeLocalDir(String tableName) {
        return jdbcTemplateUtil.getLastUpdatedTimeLocalDir(tableName);
    }

    public void updateLastUpdatedTimeAndLog(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        jdbcTemplateUtil.updateLastUpdatedTimeAndLog(tableName, timestamp, logResponseModel);
    }

    public void updateLastUpdatedTimeAndLogS3(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        jdbcTemplateUtil.updateLastUpdatedTimeAndLogS3(tableName, timestamp, logResponseModel);
    }

    public void updateLastUpdatedTimeAndLogLocalDir(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        jdbcTemplateUtil.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, logResponseModel);
    }

    public void updateLastUpdatedTime(String tableName, Timestamp timestamp) {
        jdbcTemplateUtil.updateLastUpdatedTime(tableName, timestamp);
    }

    public void deleteTable(String tableName) {
        jdbcTemplateUtil.deleteTable(tableName);
    }




    public List<PollDataSyncConfig> getTablesConfigListBySOurceDB(List<PollDataSyncConfig> configTableList, String sourceDB) {
        return configTableList.stream().filter(configObj -> Objects.equals(configObj.getSourceDb(), sourceDB)).toList();
    }

    public LogResponseModel writeJsonDataToFile(String dbSource, String tableName, Timestamp timeStamp, String jsonData) {
        return PollServiceUtil.writeJsonToFile(datasyncLocalFilePath, dbSource, tableName, timeStamp, jsonData);
    }

    public String decodeAndDecompress(String base64EncodedData) {
        return DataSimplification.decodeAndDecompress(base64EncodedData);
    }
}