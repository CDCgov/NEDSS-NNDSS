package gov.cdc.nnddatapollservice.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataPersistentService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final IRdbDataPersistentService persistentService;

    public RdbDataHandlingService(ITokenService tokenService,
                                  IRdbDataPersistentService persistentService) {
        this.tokenService = tokenService;
        this.persistentService = persistentService;
    }

    @PostConstruct
    public void handlingExchangedData() throws DataPollException {
        //Foreign Key tables
//        pollAndPeristsRDBData.handlingExchangedData("D_ORGANIZATION");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_PROVIDER");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_CASE_MANAGEMENT");
//        pollAndPeristsRDBData.handlingExchangedData("D_INTERVIEW");//no record
//        pollAndPeristsRDBData.handlingExchangedData("D_INV_ADMINISTRATIVE");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_INV_EPIDEMIOLOGY");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_INV_HIV");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_INV_LAB_FINDING");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_INV_MEDICAL_HISTORY");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_INV_RISK_FACTOR");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_INV_TREATMENT");//no record
//        pollAndPeristsRDBData.handlingExchangedData("D_INV_VACCINATION");//tested
//        pollAndPeristsRDBData.handlingExchangedData("D_PATIENT");//tested
//        pollAndPeristsRDBData.handlingExchangedData("F_INTERVIEW_CASE");//no record
//        pollAndPeristsRDBData.handlingExchangedData("F_PAGE_CASE");//tested
//        pollAndPeristsRDBData.handlingExchangedData("F_STD_PAGE_CASE");//tested
//        pollAndPeristsRDBData.handlingExchangedData("F_VAR_PAM");//no record
//        pollAndPeristsRDBData.handlingExchangedData("CONDITION");//tested
//        pollAndPeristsRDBData.handlingExchangedData("INVESTIGATION");//tested
//        pollAndPeristsRDBData.handlingExchangedData("RDB_DATE");//tested
//        pollAndPeristsRDBData.handlingExchangedData("CONFIRMATION_METHOD");//tested 1
//        pollAndPeristsRDBData.handlingExchangedData("LDF_GROUP");//not ready.
//        pollAndPeristsRDBData.handlingExchangedData("HEP_MULTI_VALUE_FIELD_GROUP");//not ready.
//        pollAndPeristsRDBData.handlingExchangedData("NOTIFICATION");//tested
//        pollAndPeristsRDBData.handlingExchangedData("PERTUSSIS_SUSPECTED_SOURCE_GRP");//not ready.
//        pollAndPeristsRDBData.handlingExchangedData("PERTUSSIS_TREATMENT_GROUP");//not ready.

//        pollAndPeristsRDBData.handlingExchangedData("BMIRD_CASE");//no record
//        pollAndPeristsRDBData.handlingExchangedData("CASE_COUNT");//Error "FK__CASE_COUN__INVES__71D87064". The conflict occurred in database "RDB", table "dbo.D_PROVIDER", column 'PROVIDER_KEY'-foreign key dependency
//        pollAndPeristsRDBData.handlingExchangedData("CONFIRMATION_METHOD_GROUP");//tested
//        pollAndPeristsRDBData.handlingExchangedData("GENERIC_CASE");//foreign key dependency LDF_GROUP
//        pollAndPeristsRDBData.handlingExchangedData("HEPATITIS_CASE");//foreign key HEP_MULTI_VALUE_FIELD_GROUP
//        pollAndPeristsRDBData.handlingExchangedData("HEPATITIS_DATAMART");//tested
//        pollAndPeristsRDBData.handlingExchangedData("LDF_DATA");//tested
//        pollAndPeristsRDBData.handlingExchangedData("LDF_FOODBORNE");//no record
//        pollAndPeristsRDBData.handlingExchangedData("MEASLES_CASE");//LDF_GROUP dependency
//        pollAndPeristsRDBData.handlingExchangedData("NOTIFICATION_EVENT");//foreign key NOTIFICATION
//        pollAndPeristsRDBData.handlingExchangedData("PERTUSSIS_CASE");//foreign key LDF_GROUP, PERTUSSIS_SUSPECTED_SOURCE_GRP, PERTUSSIS_TREATMENT_GROUP
//        pollAndPeristsRDBData.handlingExchangedData("RUBELLA_CASE");//foreign key LDF_GROUP
//        pollAndPeristsRDBData.handlingExchangedData("TREATMENT");//no record
//        pollAndPeristsRDBData.handlingExchangedData("TREATMENT_EVENT");//no record
//        pollAndPeristsRDBData.handlingExchangedData("VAR_PAM_LDF");//no record
        logger.info("---START RDB POLLING---");
        List<PollDataSyncConfig> tableList= getTableListFromConfig();
        logger.info("in data pull service tableList: " + tableList);
        int i=0;
        for (PollDataSyncConfig pollDataSyncConfig : tableList) {
            logger.info("pollDataSyncConfig: order:"+pollDataSyncConfig.getTableOrder() +"  Table:"+ pollDataSyncConfig.getTableName());
            if(!pollDataSyncConfig.getTableName().equals("LDF_GROUP")
                    && !pollDataSyncConfig.getTableName().equals("HEP_MULTI_VALUE_FIELD_GROUP")
                    && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_SUSPECTED_SOURCE_GRP")
                    && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_TREATMENT_GROUP")
                    && !pollDataSyncConfig.getTableName().equals("CASE_COUNT")
                    && !pollDataSyncConfig.getTableName().equals("GENERIC_CASE")
                    && !pollDataSyncConfig.getTableName().equals("HEPATITIS_CASE")
                    && !pollDataSyncConfig.getTableName().equals("MEASLES_CASE")
                    && !pollDataSyncConfig.getTableName().equals("NOTIFICATION_EVENT")
                    && !pollDataSyncConfig.getTableName().equals("PERTUSSIS_CASE")
                    && !pollDataSyncConfig.getTableName().equals("RUBELLA_CASE")) {

                pollAndPeristsRDBData(pollDataSyncConfig.getTableName());
            }
        }
        logger.info("---END RDB POLLING---");
    }
    private void pollAndPeristsRDBData(String tableName) throws DataPollException {
        var token = tokenService.getToken();
        var param = new HashMap<String, String>();
        logger.info("--START--handlingExchangedData for table " + tableName);
        String lastUpdatedTime= persistentService.getLastUpdatedTime(tableName);
        //call data exchange service api
        String encodedData = callDataExchangeEndpoint(token, param, tableName,"");
        //logger.info("encoded compressed data from exchange for rdb: " + encodedData);
        String rawData = decodeAndDecompress(encodedData);
        //logger.info("raw data from exchange for the table: "+tableName+" " + rawData);
        persistRdbData(tableName, rawData);

        persistentService.updateLastUpdatedTime(tableName);
        logger.info("--END--handlingExchangedData for table " + tableName);
    }
    protected String callDataExchangeEndpoint(String token, Map<String, String> params, String tableName,String lastUpdatedTime) throws DataPollException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            MultiValueMap<String, String> multiValueParams = new LinkedMultiValueMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                multiValueParams.add(entry.getKey(), entry.getValue());
            }
            String exchangeGenericEndpoint = exchangeEndpoint + "/" + tableName;
            URI uri = UriComponentsBuilder.fromHttpUrl(exchangeGenericEndpoint)
                    .queryParams(multiValueParams)
                    .build()
                    .toUri();

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
        persistentService.saveRDBData(tableName, jsonData);
    }
    private List<PollDataSyncConfig> getTableListFromConfig() throws DataPollException {
        return persistentService.getTableListFromConfig();
    }
}
