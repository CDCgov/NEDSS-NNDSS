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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
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

    @Mock
    private Gson gson;

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


    @Test
    void testHandleData1() throws DataPollException {
        dataHandlingService.exchangeEndpoint = "http://ip.jsontest.com/";
        when(icnTransportQOutService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(transportQOutService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(netsstTransportService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(tokenService.getToken()).thenReturn("Whatever");

        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        DataExchangeModel dataModel = new DataExchangeModel();
        when(gson.fromJson(anyString(), any(Class.class))).thenReturn(dataModel);

        dataHandlingService.handlingExchangedData();

        verify(tokenService, times(1)).getToken();
    }

    @Test
    void testHandleData2() throws DataPollException {
        dataHandlingService.exchangeEndpoint = "http://ip.jsontest.com/";
        when(icnTransportQOutService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(transportQOutService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(netsstTransportService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(tokenService.getToken()).thenReturn("Whatever");

        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        DataExchangeModel dataModel = new DataExchangeModel();
        dataModel.setCnTransportQOutDtoList(List.of(new CNTransportQOutDto()));
        dataModel.setTransportQOutDtoList(List.of(new TransportQOutDto()));
        dataModel.setNetssTransportQOutDtoList(List.of(new NETSSTransportQOutDto()));

        when(gson.fromJson(anyString(), any(Class.class))).thenReturn(dataModel);

        dataHandlingService.handlingExchangedData();

        verify(tokenService, times(1)).getToken();
    }


    @Test
    void testHandleData3() throws DataPollException {
        // Arrange
        dataHandlingService.exchangeEndpoint = "http://ip.jsontest.com/";
        when(icnTransportQOutService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(transportQOutService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(netsstTransportService.getMaxTimestamp()).thenReturn("2020-01-01");
        when(tokenService.getToken()).thenReturn("Whatever");

        doThrow(new RuntimeException("Whatever")).when(transportQOutService).saveDataExchange(any());

        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        DataExchangeModel dataModel = new DataExchangeModel();
        dataModel.setCnTransportQOutDtoList(List.of(new CNTransportQOutDto()));
        dataModel.setTransportQOutDtoList(List.of(new TransportQOutDto()));
        dataModel.setNetssTransportQOutDtoList(List.of(new NETSSTransportQOutDto()));

        when(gson.fromJson(anyString(), any(Class.class))).thenReturn(dataModel);

        // Act & Assert
        assertThrows(DataPollException.class, () -> dataHandlingService.handlingExchangedData());

        // Verify that the tokenService was called once
        verify(tokenService, times(1)).getToken();
    }
}
