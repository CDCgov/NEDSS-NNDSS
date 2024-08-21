package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
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
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Service
public class RdbDataHandlingService implements IRdbDataHandlingService {

    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.endpoint_generic}")
    private String exchangeEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ITokenService tokenService;
    private final RDBDataPersistentService persistentService;

    public RdbDataHandlingService(ITokenService tokenService,
                                  RDBDataPersistentService persistentService) {
        this.tokenService = tokenService;
        this.persistentService = persistentService;
    }

    public void handlingExchangedData(String tableName) throws DataPollException {
        var token = tokenService.getToken();
        var param = new HashMap<String, String>();
        //Get 'last_update_time' from data_exchange_config
        System.out.println("--START--Handling exchanged data for table " + tableName);
        String lastUpdatedTime= persistentService.getLastUpdatedTime(tableName);
        //call data exchange service api
        String encodedData = callDataExchangeEndpoint(token, param, tableName,"");
        //System.out.println("encoded compressed data from exchange for rdb: " + encodedData);
        String rawData = decodeAndDecompress(encodedData);
        System.out.println("raw data from exchange for the table: "+tableName+" " + rawData);
        persistRdbData(tableName, rawData);
        System.out.println("--END11111--Handling exchanged data for table " + tableName);
        //update 'last_update_time' in data_exchange_config
        persistentService.updateLastUpdatedTime(tableName);
        System.out.println("--END2222--Handling exchanged data for table " + tableName);
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

    public void persistRdbData(String tableName, String jsonData) throws DataPollException {
        persistentService.saveRDBData(tableName, jsonData);
    }
}
