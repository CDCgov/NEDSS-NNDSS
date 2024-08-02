package gov.cdc.nnddataexchangeservice.service;


import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.odse.CNTransportQOutRepository;
import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddataexchangeservice.service.model.dto.CNTransportQOutDto;
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

public class CNTransportQOutServiceTest {

    @Mock
    private CNTransportQOutRepository cnTransportQOutRepository;

    @InjectMocks
    private CNTransportQOutService cnTransportQOutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransportData_NoStatusTime() throws Exception {
        String statusCd = "status";
        String statusTime = "";

        CNTransportQOut cnTransportQOut = new CNTransportQOut();
        List<CNTransportQOut> cnTransportQOutList = Arrays.asList(cnTransportQOut);

        when(cnTransportQOutRepository.findTransportByStatusCd(statusCd)).thenReturn(Optional.of(cnTransportQOutList));

        List<CNTransportQOutDto> result = cnTransportQOutService.getTransportData(statusCd, statusTime);

        assertEquals(1, result.size());
        verify(cnTransportQOutRepository, times(1)).findTransportByStatusCd(statusCd);
    }

    @Test
    void testGetTransportData_WithStatusTime() throws Exception {
        String statusCd = "status";
        String statusTime = "2023-07-30 10:00:00.000";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        java.util.Date parsedDate = formatter.parse(statusTime);
        Timestamp recordStatusTime = new Timestamp(parsedDate.getTime());

        CNTransportQOut cnTransportQOut = new CNTransportQOut();
        List<CNTransportQOut> cnTransportQOutList = Arrays.asList(cnTransportQOut);

        when(cnTransportQOutRepository.findTransportByCreationTimeAndStatus(recordStatusTime, statusCd)).thenReturn(Optional.of(cnTransportQOutList));

        List<CNTransportQOutDto> result = cnTransportQOutService.getTransportData(statusCd, statusTime);

        assertEquals(1, result.size());
        verify(cnTransportQOutRepository, times(1)).findTransportByCreationTimeAndStatus(recordStatusTime, statusCd);
    }

    @Test
    void testGetTransportData_NoResults() throws Exception {
        String statusCd = "status";
        String statusTime = "";

        when(cnTransportQOutRepository.findTransportByStatusCd(statusCd)).thenReturn(Optional.of(Collections.emptyList()));

        List<CNTransportQOutDto> result = cnTransportQOutService.getTransportData(statusCd, statusTime);

        assertTrue(result.isEmpty());
        verify(cnTransportQOutRepository, times(1)).findTransportByStatusCd(statusCd);
    }

    @Test
    void testGetTransportData_Exception() {
        String statusCd = "status";
        String statusTime = "";

        when(cnTransportQOutRepository.findTransportByStatusCd(statusCd)).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataExchangeException.class, () -> cnTransportQOutService.getTransportData(statusCd, statusTime));
    }
}
