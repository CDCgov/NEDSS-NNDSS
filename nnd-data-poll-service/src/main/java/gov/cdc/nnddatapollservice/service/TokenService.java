package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService implements ITokenService {
    private static Logger logger = LoggerFactory.getLogger(TokenService.class);


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
        ResponseEntity<String> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            response = restTemplate.postForEntity(tokenEndpoint, entity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                System.exit(0);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(0);
        }
        return response.getBody();

    }
}
