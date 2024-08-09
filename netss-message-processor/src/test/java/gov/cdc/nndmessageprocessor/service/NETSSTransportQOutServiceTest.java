package gov.cdc.nndmessageprocessor.service;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.repository.NetssTransportQOutRepository;
import gov.cdc.nndmessageprocessor.repository.model.NETSSTransportQOut;
import gov.cdc.nndmessageprocessor.service.model.dto.NETSSTransportQOutDto;
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

public class NETSSTransportQOutServiceTest {

    @Mock
    private NetssTransportQOutRepository netssTransportQOutRepository;

    @InjectMocks
    private NETSSTransportQOutService netssTransportQOutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNetssCaseDataYtdAndPriorYear_Success() throws DataProcessorException {
        int currentYear = 2023;
        int currentWeek = 52;
        int priorYear = 2022;

        List<NETSSTransportQOut> mockNetssTransportQOutList = new ArrayList<>();
        mockNetssTransportQOutList.add(new NETSSTransportQOut("M012345678901234567890"));
        mockNetssTransportQOutList.add(new NETSSTransportQOut("M112345678901234567891"));

        when(netssTransportQOutRepository.findNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear))
                .thenReturn(Optional.of(mockNetssTransportQOutList));

        List<NETSSTransportQOutDto> result = netssTransportQOutService.getNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(netssTransportQOutRepository, times(1))
                .findNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear);
    }

    @Test
    void testGetNetssCaseDataYtdAndPriorYear_Empty() throws DataProcessorException {
        int currentYear = 2023;
        int currentWeek = 52;
        int priorYear = 2022;

        when(netssTransportQOutRepository.findNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear))
                .thenReturn(Optional.empty());

        List<NETSSTransportQOutDto> result = netssTransportQOutService.getNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(netssTransportQOutRepository, times(1))
                .findNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear);
    }

    @Test
    void testGetNetssCaseDataYtdAndPriorYear_Exception() {
        int currentYear = 2023;
        int currentWeek = 52;
        int priorYear = 2022;

        when(netssTransportQOutRepository.findNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear))
                .thenThrow(new RuntimeException("Database error"));

        DataProcessorException exception = assertThrows(DataProcessorException.class, () ->
                netssTransportQOutService.getNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear));

        assertEquals("Database error", exception.getMessage());
        verify(netssTransportQOutRepository, times(1))
                .findNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear);
    }

    @Test
    void testGetNetssCaseDataYtd_Success() throws DataProcessorException {
        int currentYear = 2023;
        int currentWeek = 52;

        List<NETSSTransportQOut> mockNetssTransportQOutList = new ArrayList<>();
        mockNetssTransportQOutList.add(new NETSSTransportQOut("M012345678901234567890"));
        mockNetssTransportQOutList.add(new NETSSTransportQOut("M112345678901234567891"));

        when(netssTransportQOutRepository.findNetssCaseDataYtd(currentYear, currentWeek))
                .thenReturn(Optional.of(mockNetssTransportQOutList));

        List<NETSSTransportQOutDto> result = netssTransportQOutService.getNetssCaseDataYtd(currentYear, currentWeek);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(netssTransportQOutRepository, times(1))
                .findNetssCaseDataYtd(currentYear, currentWeek);
    }

    @Test
    void testGetNetssCaseDataYtd_Empty() throws DataProcessorException {
        int currentYear = 2023;
        int currentWeek = 52;

        when(netssTransportQOutRepository.findNetssCaseDataYtd(currentYear, currentWeek))
                .thenReturn(Optional.empty());

        List<NETSSTransportQOutDto> result = netssTransportQOutService.getNetssCaseDataYtd(currentYear, currentWeek);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(netssTransportQOutRepository, times(1))
                .findNetssCaseDataYtd(currentYear, currentWeek);
    }

    @Test
    void testGetNetssCaseDataYtd_Exception() {
        int currentYear = 2023;
        int currentWeek = 52;

        when(netssTransportQOutRepository.findNetssCaseDataYtd(currentYear, currentWeek))
                .thenThrow(new RuntimeException("Database error"));

        DataProcessorException exception = assertThrows(DataProcessorException.class, () ->
                netssTransportQOutService.getNetssCaseDataYtd(currentYear, currentWeek));

        assertEquals("Database error", exception.getMessage());
        verify(netssTransportQOutRepository, times(1))
                .findNetssCaseDataYtd(currentYear, currentWeek);
    }
}
