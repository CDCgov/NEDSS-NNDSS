package gov.cdc.nnddatapollservice.service.data;

import gov.cdc.nnddatapollservice.service.data.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "tokenEndpoint", "http://example.com/token");
        ReflectionTestUtils.setField(tokenService, "clientId", "testClientId");
        ReflectionTestUtils.setField(tokenService, "clientSecret", "testClientSecret");
    }

    @Test
    void testGetToken_Success() {
        String expectedToken = "newToken";
        HttpHeaders headers = new HttpHeaders();
        headers.add("clientid", "testClientId");
        headers.add("clientsecret", "testClientSecret");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.postForEntity("http://example.com/token", entity, String.class))
                .thenReturn(ResponseEntity.ok(expectedToken));

        String token = tokenService.getToken();

        assertEquals(expectedToken, token);
        verify(restTemplate, times(1)).postForEntity("http://example.com/token", entity, String.class);
    }

    @Test
    void testFetchNewToken() {
        String expectedToken = "newToken";
        HttpHeaders headers = new HttpHeaders();
        headers.add("clientid", "testClientId");
        headers.add("clientsecret", "testClientSecret");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.postForEntity("http://example.com/token", entity, String.class))
                .thenReturn(ResponseEntity.ok(expectedToken));

        String token = ReflectionTestUtils.invokeMethod(tokenService, "fetchNewToken");

        assertEquals(expectedToken, token);
        verify(restTemplate, times(1)).postForEntity("http://example.com/token", entity, String.class);
    }
}
