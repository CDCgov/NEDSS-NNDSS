package gov.cdc.nnddataexchangeservice.service;


import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.msg.NETSSTransportQOutRepository;
import gov.cdc.nnddataexchangeservice.repository.msg.model.NETSSTransportQOut;
import gov.cdc.nnddataexchangeservice.service.model.dto.NETSSTransportQOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NetsstTransportServiceTest {

    @Mock
    private NETSSTransportQOutRepository netssTransportQOutRepository;

    @InjectMocks
    private NetsstTransportService netsstTransportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNetssTransportData_NoStatusTime() throws Exception {
        String statusTime = "";

        NETSSTransportQOut netssTransportQOut = new NETSSTransportQOut();
        List<NETSSTransportQOut> netssTransportQOutList = Arrays.asList(netssTransportQOut);

        when(netssTransportQOutRepository.findNetssTransport()).thenReturn(Optional.of(netssTransportQOutList));

        List<NETSSTransportQOutDto> result = netsstTransportService.getNetssTransportData(statusTime);

        assertEquals(1, result.size());
        verify(netssTransportQOutRepository, times(1)).findNetssTransport();
    }

    @Test
    void testGetNetssTransportData_WithStatusTime() throws Exception {
        String statusTime = "2023-07-30 10:00:00.000";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        java.util.Date parsedDate = formatter.parse(statusTime);
        Timestamp recordStatusTime = new Timestamp(parsedDate.getTime());

        NETSSTransportQOut netssTransportQOut = new NETSSTransportQOut();
        List<NETSSTransportQOut> netssTransportQOutList = Arrays.asList(netssTransportQOut);

        when(netssTransportQOutRepository.findNetssTransportByCreationTime(recordStatusTime)).thenReturn(Optional.of(netssTransportQOutList));

        List<NETSSTransportQOutDto> result = netsstTransportService.getNetssTransportData(statusTime);

        assertEquals(1, result.size());
        verify(netssTransportQOutRepository, times(1)).findNetssTransportByCreationTime(recordStatusTime);
    }

    @Test
    void testGetNetssTransportData_NoResults() throws Exception {
        String statusTime = "";

        when(netssTransportQOutRepository.findNetssTransport()).thenReturn(Optional.of(Collections.emptyList()));

        List<NETSSTransportQOutDto> result = netsstTransportService.getNetssTransportData(statusTime);

        assertTrue(result.isEmpty());
        verify(netssTransportQOutRepository, times(1)).findNetssTransport();
    }

    @Test
    void testGetNetssTransportData_Exception() {
        String statusTime = "";

        when(netssTransportQOutRepository.findNetssTransport()).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataExchangeException.class, () -> netsstTransportService.getNetssTransportData(statusTime));
    }
}