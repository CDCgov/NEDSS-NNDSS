package gov.cdc.nnddatapollservice.service;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.DataSimplification;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import gov.cdc.nnddatapollservice.share.TimestampUtil;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

@Service
@Slf4j
public class PollCommonService implements IPollCommonService {
    private final JdbcTemplateUtil jdbcTemplateUtil;

    private static Logger logger = LoggerFactory.getLogger(PollCommonService.class);

    @Value("${datasync.local_file_path}")
    private String datasyncLocalFilePath;

    @Value("${datasync.store_in_local}")
    private boolean dirSync;

    @Value("${datasync.store_in_S3}")
    private boolean s3Sync;

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public PollCommonService(JdbcTemplateUtil jdbcTemplateUtil, ITokenService tokenService) {
        this.jdbcTemplateUtil = jdbcTemplateUtil;

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

    public void updateLogNoTimestamp(String tableName, LogResponseModel logResponseModel) {
        jdbcTemplateUtil.updateLogNoTimestamp(tableName, logResponseModel);
    }

    public void deleteTable(String tableName) {
        jdbcTemplateUtil.deleteTable(tableName);
    }




    public List<PollDataSyncConfig> getTablesConfigListBySOurceDB(List<PollDataSyncConfig> configTableList, String sourceDB) {
        return configTableList.stream().filter(configObj -> Objects.equals(configObj.getSourceDb(), sourceDB)).toList();
    }

    public LogResponseModel writeJsonDataToFile(String dbSource, String tableName, Timestamp timeStamp,
                                                String jsonData, ApiResponseModel<?> apiResponseModel) {
        return PollServiceUtil.writeJsonToFile(datasyncLocalFilePath, dbSource, tableName, timeStamp, jsonData, apiResponseModel);
    }

    public String decodeAndDecompress(String base64EncodedData) {
        return DataSimplification.decodeAndDecompress(base64EncodedData);
    }

    public String getMaxId(String tableName, String key) {
        return jdbcTemplateUtil.getMaxId(tableName, key);
    }
}