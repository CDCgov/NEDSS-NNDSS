package gov.cdc.nnddatapollservice.service.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.service.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.data.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.rdb.service.RdbDataHandlingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RdbDataHandlingServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ITokenService tokenService;
    @Mock
    private RdbDataPersistentDAO rdbDataPersistentDAO;

    @InjectMocks
    private RdbDataHandlingService dataHandlingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handlingExchangedData_initialLoad() throws DataPollException {
        dataHandlingService.exchangeEndpoint = "http://ip.jsontest.com/";
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_ORGANIZATION");
        config.setLastUpdateTime(null);
        config.setTableOrder(1);
        config.setQuery("");

        configTableList.add(config);

        when(rdbDataPersistentDAO.getTableListFromConfig()).thenReturn(configTableList);
        when(tokenService.getToken()).thenReturn("testtoken");

        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        dataHandlingService.handlingExchangedData();
        verify(tokenService, times(1)).getToken();
    }

    @Test
    void handlingExchangedData_withTimestamp() throws DataPollException {
        dataHandlingService.exchangeEndpoint = "http://ip.jsontest.com/";
        List<PollDataSyncConfig> configTableList = new ArrayList<>();

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_PROVIDER");
        config.setLastUpdateTime(Timestamp.from(Instant.now()));
        config.setTableOrder(2);
        config.setQuery("");
        configTableList.add(config);

        when(rdbDataPersistentDAO.getTableListFromConfig()).thenReturn(configTableList);
        when(tokenService.getToken()).thenReturn("testtoken");

        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        dataHandlingService.handlingExchangedData();
        verify(tokenService, times(1)).getToken();
    }
}