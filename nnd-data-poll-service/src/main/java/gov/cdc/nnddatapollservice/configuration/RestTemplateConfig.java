//package gov.cdc.nnddatapollservice.configuration;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.JdkClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.http.HttpClient;
//import java.time.Duration;
//import java.util.concurrent.Executors;
//
//@Configuration
//public class RestTemplateConfig {
//
//    @Bean
//    public RestTemplate restTemplate() {
//        // Configure Java HttpClient
//        HttpClient httpClient = HttpClient.newBuilder()
//                .version(HttpClient.Version.HTTP_1_1) // Match your API Service
//                .connectTimeout(Duration.ofSeconds(10)) // 10s connect timeout
//                .executor(Executors.newVirtualThreadPerTaskExecutor()) // Use virtual threads
//                .build();
//
//        // Wrap in RestTemplate (synchronous usage)
//        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
//        factory.setReadTimeout(Duration.ofSeconds(60)); // 60s read timeout
//        return new RestTemplate(factory);
//    }
//}