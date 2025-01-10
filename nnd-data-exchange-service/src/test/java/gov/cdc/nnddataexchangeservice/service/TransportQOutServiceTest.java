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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class TransportQOutServiceTest {

    @Mock
    private TransportQOutRepository transportQOutRepository;

    @InjectMocks
    private TransportQOutService transportQOutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransportData_WithStatusTime_Success() throws DataExchangeException {
        String statusTime = "2023-07-30 12:00:00.000";
        List<TransportQOut> mockTransportQOutList = new ArrayList<>();
        mockTransportQOutList.add(new TransportQOut());
        mockTransportQOutList.add(new TransportQOut());

        when(transportQOutRepository.findTransportByCreationTime(statusTime)).thenReturn(Optional.of(mockTransportQOutList));

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime,0);

        assertNotNull(result);
       assertEquals(2, result.size());
        verify(transportQOutRepository, times(1)).findTransportByCreationTime(statusTime);
    }

    @Test
    void testGetTransportData_WithStatusTime_Success_Limit() throws DataExchangeException {
        String statusTime = "2023-07-30 12:00:00.000";
        List<TransportQOut> mockTransportQOutList = new ArrayList<>();
        mockTransportQOutList.add(new TransportQOut());
        mockTransportQOutList.add(new TransportQOut());

        when(transportQOutRepository.findTransportByCreationTimeWLimit(statusTime, 100)).thenReturn(Optional.of(mockTransportQOutList));

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime,100);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transportQOutRepository, times(1)).findTransportByCreationTimeWLimit(statusTime, 100);
    }


    @Test
    void testGetTransportData_WithoutStatusTime_Success() throws DataExchangeException {
        String statusTime = "";
        List<TransportQOut> mockTransportQOutList = new ArrayList<>();
        mockTransportQOutList.add(new TransportQOut());
        mockTransportQOutList.add(new TransportQOut());

        when(transportQOutRepository.findTransportByWithoutCreationTime()).thenReturn(Optional.of(mockTransportQOutList));

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime,0);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transportQOutRepository, times(1)).findTransportByWithoutCreationTime();
    }

    @Test
    void testGetTransportData_WithoutStatusTime_Success_Limit() throws DataExchangeException {
        String statusTime = "";
        List<TransportQOut> mockTransportQOutList = new ArrayList<>();
        mockTransportQOutList.add(new TransportQOut());
        mockTransportQOutList.add(new TransportQOut());

        when(transportQOutRepository.findTransportByWithoutCreationTimeWLimit(100)).thenReturn(Optional.of(mockTransportQOutList));

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime,100);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transportQOutRepository, times(1)).findTransportByWithoutCreationTimeWLimit(100);
    }


    @Test
    void testGetTransportData_WithStatusTime_Empty() throws DataExchangeException {
        String statusTime = "2023-07-30 12:00:00.000";

        when(transportQOutRepository.findTransportByCreationTime(statusTime)).thenReturn(Optional.empty());

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime,0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transportQOutRepository, times(1)).findTransportByCreationTime(statusTime);
    }

    @Test
    void testGetTransportData_WithoutStatusTime_Empty() throws DataExchangeException {
        String statusTime = "";

        when(transportQOutRepository.findTransportByWithoutCreationTime()).thenReturn(Optional.empty());

        List<TransportQOutDto> result = transportQOutService.getTransportData(statusTime,0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transportQOutRepository, times(1)).findTransportByWithoutCreationTime();
    }

    @Test
    void testGetTransportData_WithStatusTime_Exception() {
        String statusTime = "2023-07-30 12:00:00.000";

        when(transportQOutRepository.findTransportByCreationTime(statusTime)).thenThrow(new RuntimeException("Database error"));

        DataExchangeException exception = assertThrows(DataExchangeException.class, () ->
                transportQOutService.getTransportData(statusTime,0));

        assertEquals("Database error", exception.getMessage());
        verify(transportQOutRepository, times(1)).findTransportByCreationTime(statusTime);
    }

    @Test
    void testGetTransportData_WithoutStatusTime_Exception() {
        String statusTime = "";

        when(transportQOutRepository.findTransportByWithoutCreationTime()).thenThrow(new RuntimeException("Database error"));

        DataExchangeException exception = assertThrows(DataExchangeException.class, () ->
                transportQOutService.getTransportData(statusTime,0));

        assertEquals("Database error", exception.getMessage());
        verify(transportQOutRepository, times(1)).findTransportByWithoutCreationTime();
    }
}
