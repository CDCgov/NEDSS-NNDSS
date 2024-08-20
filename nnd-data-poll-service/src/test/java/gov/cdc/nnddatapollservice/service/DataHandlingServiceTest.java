package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddatapollservice.service.interfaces.INetsstTransportService;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.service.interfaces.ITransportQOutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
