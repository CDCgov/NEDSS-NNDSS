package gov.cdc.nnddatapollservice.service;

import com.google.gson.Gson;

import gov.cdc.nnddatapollservice.configuration.HttpClientProvider;
import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.service.interfaces.IApiService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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

    @Value("${data_exchange.endpoint_generic_total_record}")
    protected String exchangeTotalRecordEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ITokenService tokenService;

    private HttpClient httpClient;


    @Value("${data_exchange.version}")
    private String version;

    private final Gson gson = new Gson();

    private final Semaphore semaphore = new Semaphore(100); // Max 20 concurrent requests

    private final ScheduledExecutorService connectionLogger = Executors.newSingleThreadScheduledExecutor();

    public ApiService(ITokenService tokenService) {
        this.tokenService = tokenService;
        this.httpClient = HttpClientProvider.getInstance();
        connectionLogger.scheduleAtFixedRate(this::logActiveConnections, 5, 5, TimeUnit.MINUTES);

    }

    private void logActiveConnections() {
        try {
            Process process = Runtime.getRuntime().exec("netstat -anp tcp | grep ESTABLISHED | wc -l");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String activeConnections = reader.readLine().trim();
            logger.info("Active API connections: {}", activeConnections);
        } catch (IOException e) {
            logger.error("Failed to check active connections", e);
        }
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
                    .timeout(Duration.ofSeconds(120)) // Read timeout
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
                    .timeout(Duration.ofSeconds(120)) // Read timeout
//                    .header("Connection", "keep-alive")
                    .build();


            int maxRetries = 3;
            int retryDelay = 1000; // 5 seconds

            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                logger.info("API URL: {} , fire: {}", uri, attempt);
                try {
                    HttpResponse<String> response;
                    semaphore.acquire(); // Limits concurrent requests
                    try {
                        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    } finally {
                        semaphore.release();
                    }

                    if (response.statusCode() == 200) {
                        responseModel.setResponse(response.body());
                        responseModel.setSuccess(true);
                        return responseModel;
                    } else {
                        if (attempt < maxRetries) {
                            int retryDelayRandom = retryDelay * (1 << (attempt - 1)) + new Random().nextInt(500);

                            Thread.sleep(retryDelayRandom); // Exponential backoff
                        } else {
                            responseModel.setSuccess(false);
                            responseModel.setApiException(new APIException("Unexpected status code after " + maxRetries + " attempts: " + response.statusCode()));
                            return responseModel;
                        }
                    }
                } catch (Exception e) {
                    if (attempt < maxRetries) {
                        Thread.sleep(retryDelay * attempt);
                    } else {
                        responseModel.setSuccess(false);
                        responseModel.setApiException(new APIException("API request failed after " + maxRetries + " attempts: " + e.getMessage(), e));
                        return responseModel;
                    }
                }
            }


        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setApiException(new APIException("Error calling data endpoint: " + e.getMessage(), e));
            return responseModel;
        }
        responseModel.setSuccess(false);
        responseModel.setApiException(new APIException("Error calling data endpoint: " ));
        return responseModel;
    }


}
