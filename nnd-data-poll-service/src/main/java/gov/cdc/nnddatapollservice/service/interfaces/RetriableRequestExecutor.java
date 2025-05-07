package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RetriableRequestExecutor {
    <T> ApiResponseModel<T> executeWithRetry(
            Supplier<HttpRequest> requestSupplier,
            Function<HttpResponse<String>, ApiResponseModel<T>> responseHandler,
            Consumer<String> tokenRefresher,
            String apiDescription,
            int maxRetries,
            long baseDelayMs,
            Duration stuckThreshold,
            Semaphore semaphore,
            HttpClient httpClient,
            boolean iamTokenEndpoint
    );
}
