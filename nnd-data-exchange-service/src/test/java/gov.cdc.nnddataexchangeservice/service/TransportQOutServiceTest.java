package gov.cdc.nnddataexchangeservice.service;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.msg.TransportQOutRepository;
import gov.cdc.nnddataexchangeservice.repository.msg.model.TransportQOut;
import gov.cdc.nnddataexchangeservice.service.model.dto.TransportQOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransportQOutServiceTest {

    @Mock
    private TransportQOutRepository transportQOutRepository;

    @InjectMocks
    private TransportQOutService transportQOutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransportData_NoStatusTime() throws Exception {
        String statusTime = "";

        TransportQOut transportQOut = new TransportQOut();
        List<TransportQOut> transportQOutList = Arrays.asList(transportQOut);

        when(transportQOutRepository.findTransportByWithoutCreationTime()).thenReturn(Optional.of(transportQOutList));

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime);

        assertEquals(1, result.size());
        verify(transportQOutRepository, times(1)).findTransportByWithoutCreationTime();
    }

    @Test
    void testGetTransportData_WithStatusTime() throws Exception {
        String statusTime = "2023-07-30 10:00:00.000";

        TransportQOut transportQOut = new TransportQOut();
        List<TransportQOut> transportQOutList = Arrays.asList(transportQOut);

        when(transportQOutRepository.findTransportByCreationTime(statusTime)).thenReturn(Optional.of(transportQOutList));

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime);

        assertEquals(1, result.size());
        verify(transportQOutRepository, times(1)).findTransportByCreationTime(statusTime);
    }

    @Test
    void testGetTransportData_NoResults() throws Exception {
        String statusTime = "";

        when(transportQOutRepository.findTransportByWithoutCreationTime()).thenReturn(Optional.of(Collections.emptyList()));

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime);

        assertTrue(result.isEmpty());
        verify(transportQOutRepository, times(1)).findTransportByWithoutCreationTime();
    }

    @Test
    void testGetTransportData_Exception() {
        String statusTime = "";

        when(transportQOutRepository.findTransportByWithoutCreationTime()).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataExchangeException.class, () -> transportQOutService.getTransportData(statusTime));
    }
}