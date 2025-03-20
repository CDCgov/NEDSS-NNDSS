package gov.cdc.nnddatapollservice.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.service.interfaces.IApiService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.dto.TableMetaDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Service
public class ApiService implements IApiService {
    private static Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.endpoint_generic}")
    protected String exchangeEndpoint;

    @Value("${data_exchange.endpoint_generic_meta_data}")
    protected String exchangeMetaDataEndpoint;

    @Value("${data_exchange.endpoint_generic_total_record}")
    protected String exchangeTotalRecordEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ITokenService tokenService;

    private final HttpClient httpClient;

    @Value("${data_exchange.version}")
    private String version;

    private final Gson gson = new Gson();

    public ApiService(ITokenService tokenService) {
        this.tokenService = tokenService;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .executor(Executors.newVirtualThreadPerTaskExecutor()) // Optional, for async callbacks
                .build();
    }

    public ApiResponseModel<Integer> callDataCountEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime,
                                                  boolean useKeyPagination, String entityKey) {
        ApiResponseModel<Integer> responseModel = new ApiResponseModel<>();
        URI uri;
        String headerStr;
        try {


            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            headers.add("initialLoad", String.valueOf(isInitialLoad));
            headers.add("version", version);
            headers.add("useKeyPagination", String.valueOf(useKeyPagination));
            headers.add("lastKey", entityKey);

            // Build URI
            uri = UriComponentsBuilder.fromHttpUrl(exchangeTotalRecordEndpoint)
                    .path("/" + tableName)
                    .queryParamIfPresent("timestamp", Optional.ofNullable(lastUpdatedTime))
                    .build()
                    .toUri();

            // Log headers (mask sensitive info)
            HttpHeaders headersForLogging = new HttpHeaders();
            headers.entrySet().forEach(entry -> {
                String key = entry.getKey();
                if ("Authorization".equalsIgnoreCase(key) || "clientid".equalsIgnoreCase(key)
                        || "clientsecret".equalsIgnoreCase(key)) {
                    headersForLogging.add(key, "");
                } else {
                    headersForLogging.put(key, entry.getValue());
                }
            });
            headerStr = gson.toJson(headersForLogging);
            logger.info("API URL: {} , headers: {}", uri, headerStr);



            responseModel.setLastApiCall(String.valueOf(uri));
            responseModel.setLastApiHeader(headerStr);

            // Get token
            String token = tokenService.getToken();
            headers.setBearerAuth(token);

            // Build HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .headers(headers.entrySet().stream()
                            .flatMap(e -> Stream.of(e.getKey(), e.getValue().get(0)))
                            .toArray(String[]::new))
                    .timeout(Duration.ofSeconds(60)) // Read timeout
                    .build();


            // Send request synchronously
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                responseModel.setSuccess(false);
                responseModel.setApiException(new APIException("Unexpected status code: " + response.statusCode()));
                return responseModel;
            }

            responseModel.setResponse(Integer.valueOf(response.body()));
            responseModel.setSuccess(true);
            return responseModel;

        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setApiException(new APIException("Error calling data count endpoint: " + e.getMessage(), e));
            return responseModel;
        }
    }

    public ApiResponseModel<String> callDataExchangeEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime, boolean allowNull,
                                           String startRow, String endRow, boolean noPagination, boolean useKeyPagination,
                                           String entityKey) {
        ApiResponseModel<String> responseModel = new ApiResponseModel<>();
        URI uri;
        String headerStr;
        try {
            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("initialLoad", String.valueOf(isInitialLoad));
            headers.add("allowNull", String.valueOf(allowNull));
            headers.add("startRow", startRow);
            headers.add("endRow", endRow);
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            headers.add("version", version);
            headers.add("noPagination", String.valueOf(noPagination));
            headers.add("useKeyPagination", String.valueOf(useKeyPagination));
            headers.add("lastKey", entityKey);

            // Build URI
            uri = UriComponentsBuilder.fromHttpUrl(exchangeEndpoint)
                    .path("/" + tableName)
                    .queryParamIfPresent("timestamp", Optional.ofNullable(lastUpdatedTime))
                    .build()
                    .toUri();

            // Log headers (mask sensitive info)
            HttpHeaders headersForLogging = new HttpHeaders();
            headers.entrySet().forEach(entry -> {
                String key = entry.getKey();
                if ("Authorization".equalsIgnoreCase(key) || "clientid".equalsIgnoreCase(key)
                        || "clientsecret".equalsIgnoreCase(key)) {
                    headersForLogging.add(key, "");
                } else {
                    headersForLogging.put(key, entry.getValue());
                }
            });
            headerStr = gson.toJson(headersForLogging);
            logger.info("API URL: {} , headers: {}", uri, headerStr);


            responseModel.setLastApiCall(String.valueOf(uri));
            responseModel.setLastApiHeader(headerStr);

            // Get token
            String token = tokenService.getToken();
            headers.setBearerAuth(token);

            // Build HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .headers(headers.entrySet().stream()
                            .flatMap(e -> Stream.of(e.getKey(), e.getValue().get(0)))
                            .toArray(String[]::new))
                    .timeout(Duration.ofSeconds(60)) // Read timeout
                    .build();

            // Send request synchronously
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                responseModel.setSuccess(false);
                responseModel.setApiException(new APIException("Unexpected status code: " + response.statusCode()));
                return responseModel;
            }

            responseModel.setResponse(response.body());
            responseModel.setSuccess(true);
            return responseModel;

        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setApiException(new APIException("Error calling data count endpoint: " + e.getMessage(), e));
            return responseModel;
        }
    }



    public ApiResponseModel<List<TableMetaDataDto>> callMetaEndpoint(String tableName) {
        ApiResponseModel<List<TableMetaDataDto>> responseModel = new ApiResponseModel<>();
        URI uri;
        String headerStr;
        try {
            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            headers.add("version", version);

            // Build URI
            uri = UriComponentsBuilder.fromHttpUrl(exchangeMetaDataEndpoint)
                    .path("/" + tableName)
                    .build()
                    .toUri();

            // Log headers (mask sensitive info)
            HttpHeaders headersForLogging = new HttpHeaders();
            headers.entrySet().forEach(entry -> {
                String key = entry.getKey();
                if ("Authorization".equalsIgnoreCase(key) || "clientid".equalsIgnoreCase(key)
                        || "clientsecret".equalsIgnoreCase(key)) {
                    headersForLogging.add(key, "");
                } else {
                    headersForLogging.put(key, entry.getValue());
                }
            });
            headerStr = gson.toJson(headersForLogging);
            logger.info("API URL: {} , headers: {}", uri, headerStr);


            responseModel.setLastApiCall(String.valueOf(uri));
            responseModel.setLastApiHeader(headerStr);

            // Get token
            String token = tokenService.getToken();
            headers.setBearerAuth(token);

            // Build HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .headers(headers.entrySet().stream()
                            .flatMap(e -> Stream.of(e.getKey(), e.getValue().get(0)))
                            .toArray(String[]::new))
                    .timeout(Duration.ofSeconds(60)) // Read timeout
                    .build();

            // Send request synchronously
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                responseModel.setSuccess(false);
                responseModel.setApiException(new APIException("Unexpected status code: " + response.statusCode()));
                return responseModel;
            }

            var gsonWithUpperCaseSupport = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .create();
            Type listType = new TypeToken<List<TableMetaDataDto>>() {}.getType();

            List<TableMetaDataDto> metaDataList = gsonWithUpperCaseSupport.fromJson(response.body(),
                    listType);


            responseModel.setResponse(metaDataList);
            responseModel.setSuccess(true);
            return responseModel;

        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setApiException(new APIException("Error calling data count endpoint: " + e.getMessage(), e));
            return responseModel;
        }
    }

}
