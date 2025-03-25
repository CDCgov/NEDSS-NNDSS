package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.configuration.HttpClientProvider;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
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
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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

    private HttpClient httpClient;

    private final ScheduledExecutorService connectionLogger = Executors.newSingleThreadScheduledExecutor();

    public TokenService() {
        this.httpClient = HttpClientProvider.getInstance();
        connectionLogger.scheduleAtFixedRate(this::logActiveConnections, 5, 5, TimeUnit.MINUTES);
    }

    private void logActiveConnections() {
        try {
            Process process = Runtime.getRuntime().exec("netstat -anp tcp | grep ESTABLISHED | wc -l");
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
        try {
            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(CLIENT_ID, clientId);
            headers.add(CLIENT_SECRET, clientSecret);

            // Build HttpRequest (POST with no body)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenEndpoint))
                    .POST(HttpRequest.BodyPublishers.noBody()) // Empty body for POST
                    .headers(headers.entrySet().stream()
                            .flatMap(e -> Stream.of(e.getKey(), e.getValue().get(0)))
                            .toArray(String[]::new))
                    .timeout(Duration.ofSeconds(30)) // 30s timeout for token fetch
                    .build();

            this.httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(10))
                    .executor(Executors.newVirtualThreadPerTaskExecutor()) // Optional, for async callbacks
                    .followRedirects(HttpClient.Redirect.NORMAL) // Handles redirects properly
                    .build();

            // Send request synchronously
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                logger.error("Failed to fetch token, status code: {}", response.statusCode());
                System.exit(0); // Preserve original behavior
            }
            return response.body();

        } catch (Exception e) {
            logger.error("Error fetching token: {}", e.getMessage(), e);
            System.exit(0); // Preserve original behavior
            return null; // Unreachable, but added for compiler
        }
    }
}