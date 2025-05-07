package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.configuration.HttpClientProvider;
import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.interfaces.RetriableRequestExecutor;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static gov.cdc.nnddatapollservice.constant.ApiConstantValue.CLIENT_ID;
import static gov.cdc.nnddatapollservice.constant.ApiConstantValue.CLIENT_SECRET;

@Service
public class TokenService implements ITokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${data_exchange.endpoint_token}")
    private String tokenEndpoint;

    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.retry.max-attempts:5}")
    private int maxRetries;

    @Value("${data_exchange.retry.delay-ms:3000}")
    private long retryDelayMs;

    @Value("${data_exchange.retry.stuck-threshold-minutes:10}")
    private int stuckThresholdMinutes;


    private HttpClient httpClient;

    private final RetriableRequestExecutor retrier;


    private final ScheduledExecutorService connectionLogger = Executors.newSingleThreadScheduledExecutor();

    public TokenService(RetriableRequestExecutor retrier) {
        this.retrier = retrier;
        this.httpClient = HttpClientProvider.getInstance();
        connectionLogger.scheduleAtFixedRate(this::logActiveConnections, 5, 5, TimeUnit.MINUTES);
    }

    private void logActiveConnections() {
        try {
            Process process = Runtime.getRuntime().exec("netstat -anp tcp | grep ESTABLISHED | wc -l"); //NOSONAR
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String activeConnections = reader.readLine().trim();
            logger.info("Active API (TOKEN) connections: {}", activeConnections);
        } catch (IOException e) {
            logger.error("Failed to check active connections", e);
        }
    }

    @Override
    public String getToken() {
        return fetchNewToken();
    }

    private String fetchNewToken() {
        ApiResponseModel<String> tokenResponse = retrier.executeWithRetry(
                this::buildTokenRequest,
                response -> {
                    ApiResponseModel<String> model = new ApiResponseModel<>();
                    if (response.statusCode() == 200) {
                        model.setResponse(response.body());
                        model.setSuccess(true);
                    } else {
                        logger.error("Token fetch failed. Status code: {}", response.statusCode());
                        model.setSuccess(false);
                        model.setApiException(new APIException("Token fetch failed with status: " + response.statusCode()));
                    }
                    return model;
                },
                ignore -> {
                    // Nothing to refresh during token fetch
                },
                "Token Fetch",
                maxRetries,
                retryDelayMs,
                Duration.ofMinutes(stuckThresholdMinutes),
                new Semaphore(1),
                httpClient,
                true
        );

        if (!tokenResponse.isSuccess()) {
            logger.error("Token fetch failed after {} retries. Shutting down.", maxRetries);
            System.exit(0);
        }

        return tokenResponse.getResponse();
    }

    private HttpRequest buildTokenRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CLIENT_ID, clientId);
        headers.add(CLIENT_SECRET, clientSecret);

        return HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .POST(HttpRequest.BodyPublishers.noBody())
                .headers(headers.entrySet().stream()
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue().get(0)))
                        .toArray(String[]::new))
                .timeout(Duration.ofSeconds(30))
                .build();
    }
}