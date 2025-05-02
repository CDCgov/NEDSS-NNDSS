package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.configuration.HttpClientProvider;
import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.service.interfaces.RetriableRequestExecutor;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class RetriableRequestExecutorImpl implements RetriableRequestExecutor {
    private static final Logger logger = LoggerFactory.getLogger(RetriableRequestExecutorImpl.class);
    @Value("${data_exchange.retry.max-backoff-ms:60000}")
    private int maxBackoffMs;

    @Override
    public <T> ApiResponseModel<T> executeWithRetry(Supplier<HttpRequest> requestSupplier,
                                                    Function<HttpResponse<String>, ApiResponseModel<T>> responseHandler,
                                                    Consumer<String> tokenRefresher,
                                                    String apiDescription,
                                                    int maxRetries,
                                                    long baseDelayMs,
                                                    Duration stuckThreshold,
                                                    Semaphore semaphore,
                                                    HttpClient httpClient,
                                                    boolean iamTokenEndpoint) {
        Instant start = Instant.now();
        boolean tokenRefreshed = false;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            logger.debug("{} - Attempt {}/{}", apiDescription, attempt, maxRetries);
            try {
                semaphore.acquire();
                try {
                    HttpRequest request = requestSupplier.get();
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    ApiResponseModel<T> result = responseHandler.apply(response);
                    if (Boolean.TRUE.equals(result.isSuccess())) {
                        return result;
                    }

                    if (!iamTokenEndpoint && !tokenRefreshed && Duration.between(start, Instant.now()).compareTo(stuckThreshold) > 0) {
                        logger.warn("{} - Token likely expired, refreshing...", apiDescription);
                        tokenRefresher.accept("force");
                        tokenRefreshed = true;
                    }

                } finally {
                    semaphore.release();
                }
            } catch (Exception ex) {
                logger.error("{} - Attempt {} failed: {}", apiDescription, attempt, ex.getMessage(), ex);
            }

            // Apply exponential backoff
            try {
                long backoff = Math.min(baseDelayMs * (1L << (attempt - 1)), maxBackoffMs);
                logger.info("{} - Sleeping for {} ms before retry", apiDescription, backoff);
                Thread.sleep(backoff);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        ApiResponseModel<T> fail = new ApiResponseModel<>();
        fail.setSuccess(false);
        fail.setApiException(new APIException(apiDescription + " failed after " + maxRetries + " attempts."));
        return fail;
    }
}
