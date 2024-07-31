package gov.cdc.nnddataexchangeservice.service;


import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import gov.cdc.nnddataexchangeservice.service.interfaces.INetsstTransportService;
import gov.cdc.nnddataexchangeservice.service.interfaces.ITransportQOutService;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;
import gov.cdc.nnddataexchangeservice.service.model.dto.CNTransportQOutDto;
import gov.cdc.nnddataexchangeservice.service.model.dto.NETSSTransportQOutDto;
import gov.cdc.nnddataexchangeservice.service.model.dto.TransportQOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataExchangeServiceTest {

    @Mock
    private INetsstTransportService netsstTransportService;

    @Mock
    private ITransportQOutService transportQOutService;

    @Mock
    private ICNTransportQOutService cnTransportQOutService;

    @InjectMocks
    private DataExchangeService dataExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDataForOnPremExchanging() throws Exception {
        String cnStatusTime = "2023-07-30 10:00:00.000";
        String transportTime = "2023-07-30 10:00:00.000";
        String netssTime = "2023-07-30 10:00:00.000";
        String statusCd = "status";

        List<CNTransportQOutDto> cnTransportQOutDtos = Collections.singletonList(new CNTransportQOutDto());
        List<TransportQOutDto> transportQOutDtos = Collections.singletonList(new TransportQOutDto());
        List<NETSSTransportQOutDto> netssTransportQOutDtos = Collections.singletonList(new NETSSTransportQOutDto());

        when(cnTransportQOutService.getTransportData(statusCd, cnStatusTime)).thenReturn(cnTransportQOutDtos);
        when(transportQOutService.getTransportData(transportTime)).thenReturn(transportQOutDtos);
        when(netsstTransportService.getNetssTransportData(netssTime)).thenReturn(netssTransportQOutDtos);

        DataExchangeModel result = dataExchangeService.getDataForOnPremExchanging(cnStatusTime, transportTime, netssTime, statusCd);

        assertNotNull(result);
        assertEquals(cnTransportQOutDtos.size(), result.getCountCnTransport());
        assertEquals(transportQOutDtos.size(), result.getCountTransport());
        assertEquals(netssTransportQOutDtos.size(), result.getCountNetssTransport());

        verify(cnTransportQOutService, times(1)).getTransportData(statusCd, cnStatusTime);
        verify(transportQOutService, times(1)).getTransportData(transportTime);
        verify(netsstTransportService, times(1)).getNetssTransportData(netssTime);
    }

    @Test
    void testGetDataForOnPremExchanging_Exception() throws Exception {
        String cnStatusTime = "2023-07-30 10:00:00.000";
        String transportTime = "2023-07-30 10:00:00.000";
        String netssTime = "2023-07-30 10:00:00.000";
        String statusCd = "status";

        when(cnTransportQOutService.getTransportData(statusCd, cnStatusTime)).thenThrow(new DataExchangeException("Exception"));

        assertThrows(DataExchangeException.class, () -> dataExchangeService.getDataForOnPremExchanging(cnStatusTime, transportTime, netssTime, statusCd));

        verify(cnTransportQOutService, times(1)).getTransportData(statusCd, cnStatusTime);
        verify(transportQOutService, times(0)).getTransportData(transportTime);
        verify(netsstTransportService, times(0)).getNetssTransportData(netssTime);
    }
}