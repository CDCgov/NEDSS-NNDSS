package gov.cdc.nnddatapollservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.*;
import gov.cdc.nnddatapollservice.service.model.DataExchangeModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DataHandlingServiceTest {

    @Mock
    private ICNTransportQOutService icnTransportQOutService;

    @Mock
    private INetsstTransportService netsstTransportService;

    @Mock
    private ITransportQOutService transportQOutService;

    @Mock
    private ITokenService tokenService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DataHandlingService dataHandlingService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        // Set the exchangeEndpoint value using reflection
        Field exchangeEndpointField = DataHandlingService.class.getDeclaredField("exchangeEndpoint");
        exchangeEndpointField.setAccessible(true);
        exchangeEndpointField.set(dataHandlingService, "http://example.com/exchange");
    }


    @Test
    void testPersistingExchangeData_Success() throws DataPollException {
        String data = "{\"cnTransportQOutDtoList\":[], \"transportQOutDtoList\":[], \"netssTransportQOutDtoList\":[]}";

        dataHandlingService.persistingExchangeData(data);

        verify(icnTransportQOutService, never()).saveDataExchange(anyList());
        verify(transportQOutService, never()).saveDataExchange(anyList());
        verify(netsstTransportService, never()).saveDataExchange(anyList());
    }

    @Test
    void testPersistingExchangeData_Exception() throws DataPollException {
        String data = "invalid json";

        assertThrows(DataPollException.class, () -> dataHandlingService.persistingExchangeData(data));

        verify(icnTransportQOutService, never()).saveDataExchange(anyList());
        verify(transportQOutService, never()).saveDataExchange(anyList());
        verify(netsstTransportService, never()).saveDataExchange(anyList());
    }

    @Test
    void testTruncatingDataForFullLoading() {
        dataHandlingService.fullLoadApplied = true;
        dataHandlingService.truncatingDataForFullLoading();
        verify(icnTransportQOutService).truncatingData();
        verify(transportQOutService).truncatingData();
        verify(netsstTransportService).truncatingData();
    }


    @Test
    void testTruncatingDataForFullLoadingFalse() {
        dataHandlingService.fullLoadApplied = false;
        dataHandlingService.truncatingDataForFullLoading();
        verify(icnTransportQOutService, never()).truncatingData();
        verify(transportQOutService, never()).truncatingData();
        verify(netsstTransportService, never()).truncatingData();
    }

}
