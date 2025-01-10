package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.repository.msg.TransportQOutRepository;
import gov.cdc.nnddatapollservice.service.interfaces.IErrorHandlingService;
import gov.cdc.nnddatapollservice.service.model.dto.TransportQOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransportQOutServiceTest {

    @Mock
    private TransportQOutRepository transportQOutRepository;

    @Mock
    private IErrorHandlingService errorHandlingService;

    @InjectMocks
    private TransportQOutService transportQOutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(transportQOutService, "fileLocation", System.getProperty("java.io.tmpdir"));
    }

    @Test
    void testGetMaxTimestamp_WithTimestamp() throws DataPollException {
        Timestamp maxTimestamp = new Timestamp(System.currentTimeMillis());
        when(transportQOutRepository.findMaxTimeStampInvolvingWithNotification()).thenReturn(Optional.of(maxTimestamp.toString()));

        String result = transportQOutService.getMaxTimestamp();

        assertEquals(maxTimestamp.toString(), result);
        verify(transportQOutRepository, times(1)).findMaxTimeStampInvolvingWithNotification();
    }

    @Test
    void testGetMaxTimestamp_NoTimestamp() throws DataPollException {
        when(transportQOutRepository.findMaxTimeStampInvolvingWithNotification()).thenReturn(Optional.empty());

        String result = transportQOutService.getMaxTimestamp();

        assertEquals("", result);
        verify(transportQOutRepository, times(1)).findMaxTimeStampInvolvingWithNotification();
    }

    @Test
    void testGetMaxTimestamp_Exception() {
        when(transportQOutRepository.findMaxTimeStampInvolvingWithNotification()).thenThrow(new RuntimeException("Exception"));

        assertThrows(DataPollException.class, () -> transportQOutService.getMaxTimestamp());

        verify(transportQOutRepository, times(1)).findMaxTimeStampInvolvingWithNotification();
    }

    @Test
    void testSaveDataExchange_Success() throws DataPollException {
        List<TransportQOutDto> transportQOutDtoList = new ArrayList<>();
        transportQOutDtoList.add(new TransportQOutDto());
        transportQOutDtoList.add(new TransportQOutDto());

        transportQOutService.saveDataExchange(transportQOutDtoList);

        verify(transportQOutRepository, times(1)).saveAll(anyList());
        verify(transportQOutRepository, times(1)).flush();
    }

    @Test
    void testSaveDataExchange_PartialFailure() throws DataPollException {
        List<TransportQOutDto> transportQOutDtoList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            transportQOutDtoList.add(new TransportQOutDto());
        }

        doThrow(new RuntimeException("Batch save failed")).when(transportQOutRepository).saveAll(anyList());

        transportQOutService.saveDataExchange(transportQOutDtoList);

        verify(transportQOutRepository, times(1)).saveAll(anyList());
        verify(transportQOutRepository, never()).flush();
        verify(errorHandlingService, atLeastOnce()).dumpBatchToFile(anyList(), anyString(), anyString());
    }

    @Test
    void testSaveDataExchange_Exception() {
        List<TransportQOutDto> transportQOutDtoList = new ArrayList<>();
        transportQOutDtoList.add(new TransportQOutDto());

        doThrow(new RuntimeException("Exception")).when(transportQOutRepository).saveAll(anyList());
        doThrow(new RuntimeException("Exception")).when(errorHandlingService).dumpBatchToFile(anyList(), anyString(), anyString());

        assertThrows(DataPollException.class, () -> transportQOutService.saveDataExchange(transportQOutDtoList));

        verify(transportQOutRepository, times(1)).saveAll(anyList());
        verify(errorHandlingService, atLeastOnce()).dumpBatchToFile(anyList(), anyString(), anyString());
    }

    @Test
    void testTruncating() {
        transportQOutService.truncatingData();
        verify(transportQOutRepository, times(1)).truncateTable();
    }
}
