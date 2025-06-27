package gov.cdc.nnddatapollservice.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.configuration.HttpClientProvider;
import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.service.interfaces.IApiService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.interfaces.RetriableRequestExecutor;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.dto.TableMetaDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static gov.cdc.nnddatapollservice.constant.ApiConstantValue.*;

@Service
public class ApiService implements IApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

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

    @Value("${data_exchange.version}")
    private String version;

    @Value("${data_exchange.retry.max-attempts:5}")
    private int maxRetries;

    @Value("${data_exchange.retry.delay-ms:3000}")
    private long retryDelayMs;

    @Value("${data_exchange.retry.stuck-threshold-minutes:10}")
    private int stuckThresholdMinutes;

    private final ITokenService tokenService;
    private final RetriableRequestExecutor retrier;
    private final HttpClient httpClient;
    private final Gson gson = new Gson();
    private final Semaphore semaphore = new Semaphore(100);
    private final ScheduledExecutorService connectionLogger = Executors.newSingleThreadScheduledExecutor();

    public ApiService(ITokenService tokenService, RetriableRequestExecutor retrier) {
        this.tokenService = tokenService;
        this.retrier = retrier;
        this.httpClient = HttpClientProvider.getInstance();
        connectionLogger.scheduleAtFixedRate(this::logActiveConnections, 5, 5, TimeUnit.MINUTES);
    }

    private void logActiveConnections() {
        try {
            Process process = Runtime.getRuntime().exec("netstat -anp tcp | grep ESTABLISHED | wc -l");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String activeConnections = reader.readLine().trim();
            logger.info("Active API (MAIN) connections: {}", activeConnections);
        } catch (IOException e) {
            logger.error("Failed to check active connections", e);
        }
    }

    private String maskHeaders(HttpHeaders headers) {
        HttpHeaders masked = new HttpHeaders();
        headers.forEach((key, value) -> {
            if (AUTHORIZATION.equalsIgnoreCase(key) || CLIENT_ID.equalsIgnoreCase(key) || CLIENT_SECRET.equalsIgnoreCase(key)) {
                masked.add(key, "");
            } else {
                masked.put(key, value);
            }
        });
        return gson.toJson(masked);
    }

    private HttpRequest buildRequest(URI uri, HttpHeaders headers, Duration timeout) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .headers(headers.entrySet().stream()
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue().get(0)))
                        .toArray(String[]::new))
                .timeout(timeout)
                .build();
    }

    @Override
    public ApiResponseModel<Integer> callDataCountEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime,
                                                           boolean useKeyPagination, String entityKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CLIENT_ID, clientId);
        headers.add(CLIENT_SECRET, clientSecret);
        headers.add(INITIAL_LOAD, String.valueOf(isInitialLoad));
        headers.add(VERSION, version);
        headers.add(USE_KEY_PAGINATION, String.valueOf(useKeyPagination));
        headers.add(LAST_KEY, entityKey);

        URI uri = UriComponentsBuilder.fromHttpUrl(exchangeTotalRecordEndpoint)
                .path("/" + tableName)
                .queryParamIfPresent(TIMESTAMP, Optional.ofNullable(lastUpdatedTime))
                .build()
                .toUri();

        String fullUrl = uri.toString();
        String headerString = maskHeaders(headers);

        logger.info("Count API URL: {} , headers: {}", uri, headerString);


        return retrier.executeWithRetry(
                () -> {
                    String token = tokenService.getToken();
                    headers.setBearerAuth(token);
                    return buildRequest(uri, headers, Duration.ofSeconds(120));
                },
                response -> {
                    ApiResponseModel<Integer> model = new ApiResponseModel<>();
                    model.setLastApiCall(fullUrl);
                    model.setLastApiHeader(headerString);

                    if (response.statusCode() == 200) {
                        model.setResponse(Integer.valueOf(response.body()));
                        model.setSuccess(true);
                    } else {
                        model.setSuccess(false);
                        model.setApiException(new APIException("Unexpected status code: " + response.statusCode()));
                    }
                    return model;
                },
                ignore -> tokenService.getToken(),
                "Count API Call",
                maxRetries,
                retryDelayMs,
                Duration.ofMinutes(stuckThresholdMinutes),
                semaphore,
                httpClient,
                false
        );
    }

    @Override
    public ApiResponseModel<String> callDataExchangeEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime, boolean allowNull,
                                                             String startRow, String endRow, boolean noPagination, boolean useKeyPagination,
                                                             String entityKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(INITIAL_LOAD, String.valueOf(isInitialLoad));
        headers.add(ALLOW_NULL, String.valueOf(allowNull));
        headers.add(START_ROW, startRow);
        headers.add(END_ROW, endRow);
        headers.add(CLIENT_ID, clientId);
        headers.add(CLIENT_SECRET, clientSecret);
        headers.add(VERSION, version);
        headers.add(NO_PAGINATION, String.valueOf(noPagination));
        headers.add(USE_KEY_PAGINATION, String.valueOf(useKeyPagination));
        headers.add(LAST_KEY, entityKey);

        URI uri = UriComponentsBuilder.fromHttpUrl(exchangeEndpoint)
                .path("/" + tableName)
                .queryParamIfPresent(TIMESTAMP, Optional.ofNullable(lastUpdatedTime))
                .build()
                .toUri();

        String fullUrl = uri.toString();
        String headerString = maskHeaders(headers);

        logger.info("DataSync API URL: {} , headers: {}", uri, maskHeaders(headers));

        return retrier.executeWithRetry(
                () -> {
                    String token = tokenService.getToken();
                    headers.setBearerAuth(token);
                    return buildRequest(uri, headers, Duration.ofSeconds(120));
                },
                response -> {
                    ApiResponseModel<String> model = new ApiResponseModel<>();
                    model.setLastApiCall(fullUrl);
                    model.setLastApiHeader(headerString);
                    if (response.statusCode() == 200) {
                        model.setResponse(response.body());
                        model.setSuccess(true);
                    } else {
                        model.setSuccess(false);
                        model.setApiException(new APIException("Unexpected status code: " + response.statusCode()));
                    }
                    return model;
                },
                ignore -> tokenService.getToken(),
                "Data Exchange API Call",
                maxRetries,
                retryDelayMs,
                Duration.ofMinutes(stuckThresholdMinutes),
                semaphore,
                httpClient,
                false
        );
    }

    @Override
    public ApiResponseModel<List<TableMetaDataDto>> callMetaEndpoint(String tableName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("clientid", clientId);
        headers.add("clientsecret", clientSecret);
        headers.add("version", version);

        URI uri = UriComponentsBuilder.fromHttpUrl(exchangeMetaDataEndpoint)
                .path("/" + tableName)
                .build()
                .toUri();

        logger.info("Meta API URL: {} , headers: {}", uri, maskHeaders(headers));
        String fullUrl = uri.toString();
        String headerString = maskHeaders(headers);
        return retrier.executeWithRetry(
                () -> {
                    String token = tokenService.getToken();
                    headers.setBearerAuth(token);
                    return buildRequest(uri, headers, Duration.ofSeconds(60));
                },
                response -> {
                    ApiResponseModel<List<TableMetaDataDto>> model = new ApiResponseModel<>();
                    model.setLastApiCall(fullUrl);
                    model.setLastApiHeader(headerString);
                    if (response.statusCode() == 200) {
                        var gsonWithUpperCase = new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                                .create();
                        Type listType = new TypeToken<List<TableMetaDataDto>>() {}.getType();
                        model.setResponse(gsonWithUpperCase.fromJson(response.body(), listType));
                        model.setSuccess(true);
                    } else {
                        model.setSuccess(false);
                        model.setApiException(new APIException("Unexpected status code: " + response.statusCode()));
                    }
                    return model;
                },
                ignore -> tokenService.getToken(),
                "Meta Data API Call",
                maxRetries,
                retryDelayMs,
                Duration.ofMinutes(stuckThresholdMinutes),
                semaphore,
                httpClient,
                false
        );
    }
}

