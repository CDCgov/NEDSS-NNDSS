package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.repository.odse.CNTransportQOutRepository;
import gov.cdc.nnddatapollservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddatapollservice.service.interfaces.IErrorHandlingService;
import gov.cdc.nnddatapollservice.service.model.dto.CNTransportQOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CNTransportQOutServiceTest {

    @Mock
    private CNTransportQOutRepository cnTransportQOutRepository;

    @Mock
    private IErrorHandlingService errorHandlingService;

    @InjectMocks
    private CNTransportQOutService cnTransportQOutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMaxTimestamp_WithTimestamp() {
        Timestamp maxTimestamp = new Timestamp(System.currentTimeMillis());
        when(cnTransportQOutRepository.findMaxTimeStamp()).thenReturn(Optional.of(maxTimestamp));

        String result = cnTransportQOutService.getMaxTimestamp();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        assertEquals(formatter.format(maxTimestamp), result);
        verify(cnTransportQOutRepository, times(1)).findMaxTimeStamp();
    }

    @Test
    void testGetMaxTimestamp_NoTimestamp() {
        when(cnTransportQOutRepository.findMaxTimeStamp()).thenReturn(Optional.empty());

        String result = cnTransportQOutService.getMaxTimestamp();

        assertEquals("", result);
        verify(cnTransportQOutRepository, times(1)).findMaxTimeStamp();
    }

    @Test
    void testSaveDataExchange_Success() throws DataPollException {
        List<CNTransportQOutDto> transportQOutDtoList = new ArrayList<>();
        transportQOutDtoList.add(new CNTransportQOutDto());
        transportQOutDtoList.add(new CNTransportQOutDto());

        cnTransportQOutService.saveDataExchange(transportQOutDtoList);

        verify(cnTransportQOutRepository, times(1)).saveAll(anyList());
        verify(cnTransportQOutRepository, times(1)).flush();
    }

    @Test
    void testSaveDataExchange_PartialFailure() throws Exception {
        List<CNTransportQOutDto> transportQOutDtoList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            transportQOutDtoList.add(new CNTransportQOutDto());
        }


        doThrow(new RuntimeException("Batch save failed")).when(cnTransportQOutRepository).saveAll(anyList());

        cnTransportQOutService.saveDataExchange(transportQOutDtoList);

        verify(errorHandlingService, atLeastOnce()).dumpBatchToFile(any(), any(), any());
    }

    @Test
    void testSaveDataExchange_Exception() {
        List<CNTransportQOutDto> transportQOutDtoList = new ArrayList<>();
        transportQOutDtoList.add(new CNTransportQOutDto());

        doThrow(new RuntimeException("Exception")).when(cnTransportQOutRepository).saveAll(anyList());
        doThrow(new RuntimeException("Exception")).when(errorHandlingService).dumpBatchToFile(any(), any(), any());


        assertThrows(DataPollException.class, () -> cnTransportQOutService.saveDataExchange(transportQOutDtoList));


    }

    @Test
    void testTruncating() {
        cnTransportQOutService.truncatingData();
        verify(cnTransportQOutRepository, times(1)).truncateTable();
    }
}
