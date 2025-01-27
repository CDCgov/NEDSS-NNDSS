package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService implements ITokenService {
    @Value("${data_exchange.endpoint_token}")
    private String tokenEndpoint;

    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public TokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getToken() {
        return fetchNewToken();
    }

    private String fetchNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("clientid", clientId);
        headers.add("clientsecret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, entity, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            System.exit(0);
        }
        return response.getBody();    }
}
