package gov.cdc.nnddatapollservice.service;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddatapollservice.service.interfaces.INetsstTransportService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.interfaces.ITransportQOutService;
import gov.cdc.nnddatapollservice.service.model.DataExchangeModel;
import gov.cdc.nnddatapollservice.service.model.dto.CNTransportQOutDto;
import gov.cdc.nnddatapollservice.service.model.dto.NETSSTransportQOutDto;
import gov.cdc.nnddatapollservice.service.model.dto.TransportQOutDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class NNDDataHandlingServiceTest {

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

    @Mock
    private Gson gson;

    @InjectMocks
    private NNDDataHandlingService dataHandlingService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        // Set the exchangeEndpoint value using reflection
        Field exchangeEndpointField = NNDDataHandlingService.class.getDeclaredField("exchangeEndpoint");
        exchangeEndpointField.setAccessible(true);
        exchangeEndpointField.set(dataHandlingService, "http://example.com/exchange");
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
