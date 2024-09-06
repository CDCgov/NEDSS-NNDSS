package gov.cdc.nnddatapollservice.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
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
    void setUp() throws NoSuchFieldException, IllegalAccessException {
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

        PollDataSyncConfig config1 = new PollDataSyncConfig();
        config1.setTableName("D_PROVIDER");
        config1.setLastUpdateTime(Timestamp.from(Instant.now()));
        config1.setTableOrder(2);
        config1.setQuery("");
        configTableList.add(config1);

        when(rdbDataPersistentDAO.getTableListFromConfig()).thenReturn(configTableList);
        when(tokenService.getToken()).thenReturn("testtoken");

        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        dataHandlingService.handlingExchangedData();
        verify(tokenService, times(1)).getToken();
    }
}