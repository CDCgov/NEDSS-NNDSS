package gov.cdc.nnddatapollservice.configuration;

import java.net.CookieManager;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpClientProvider {
    private static volatile HttpClient HTTP_CLIENT = createHttpClient();

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    private static HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .executor(Executors.newVirtualThreadPerTaskExecutor()) // Virtual threads
                .followRedirects(HttpClient.Redirect.NORMAL)
                .cookieHandler(new CookieManager()) // Enables connection reuse
                .build();
    }


    static {
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (HttpClientProvider.class) {
                HTTP_CLIENT = createHttpClient(); // Restart HttpClient every 15 minutes
                System.out.println("Restarted HttpClient to prevent connection exhaustion.");
            }
        }, 15, 15, TimeUnit.MINUTES);
    }


    private HttpClientProvider() {}

    public static HttpClient getInstance() {
        return HTTP_CLIENT;
    }
}