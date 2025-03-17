package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

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


    public TokenService() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10)) // 10s connect timeout
                .executor(Executors.newVirtualThreadPerTaskExecutor()) // Optional, for async callbacks
                .build();
    }

    @Override
    public String getToken() {
        return fetchNewToken();
    }

    private String fetchNewToken() {
        try {
            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);

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