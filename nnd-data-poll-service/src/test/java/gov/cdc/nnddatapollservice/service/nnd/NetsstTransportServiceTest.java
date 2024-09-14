package gov.cdc.nnddatapollservice.service.nnd;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.repository.msg.NETSSTransportQOutRepository;
import gov.cdc.nnddatapollservice.service.data.model.dto.NETSSTransportQOutDto;
import gov.cdc.nnddatapollservice.service.nnd.interfaces.IErrorHandlingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NetsstTransportServiceTest {

    @Mock
    private NETSSTransportQOutRepository netssTransportQOutRepository;

    @Mock
    private IErrorHandlingService errorHandlingService;

    @InjectMocks
    private NetsstTransportService netsstTransportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(netsstTransportService, "fileLocation", System.getProperty("java.io.tmpdir"));
    }

    @Test
    void testGetMaxTimestamp_WithTimestamp() {
        Timestamp maxTimestamp = new Timestamp(System.currentTimeMillis());
        when(netssTransportQOutRepository.findMaxTimeStamp()).thenReturn(Optional.of(maxTimestamp));

        String result = netsstTransportService.getMaxTimestamp();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        assertEquals(formatter.format(maxTimestamp), result);
        verify(netssTransportQOutRepository, times(1)).findMaxTimeStamp();
    }

    @Test
    void testGetMaxTimestamp_NoTimestamp() {
        when(netssTransportQOutRepository.findMaxTimeStamp()).thenReturn(Optional.empty());

        String result = netsstTransportService.getMaxTimestamp();

        assertEquals("", result);
        verify(netssTransportQOutRepository, times(1)).findMaxTimeStamp();
    }

    @Test
    void testSaveDataExchange_Success() throws DataPollException {
        List<NETSSTransportQOutDto> transportQOutDtoList = new ArrayList<>();
        transportQOutDtoList.add(new NETSSTransportQOutDto());
        transportQOutDtoList.add(new NETSSTransportQOutDto());

        netsstTransportService.saveDataExchange(transportQOutDtoList);

        verify(netssTransportQOutRepository, times(1)).saveAll(anyList());
        verify(netssTransportQOutRepository, times(1)).flush();
    }

    @Test
    void testSaveDataExchange_PartialFailure() throws Exception {
        List<NETSSTransportQOutDto> transportQOutDtoList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            transportQOutDtoList.add(new NETSSTransportQOutDto());
        }

        doThrow(new RuntimeException("Batch save failed")).when(netssTransportQOutRepository).saveAll(anyList());

        netsstTransportService.saveDataExchange(transportQOutDtoList);

        verify(netssTransportQOutRepository, times(1)).saveAll(anyList());
        verify(netssTransportQOutRepository, never()).flush();
        verify(errorHandlingService, atLeastOnce()).dumpBatchToFile(anyList(), anyString(), anyString());
    }

    @Test
    void testSaveDataExchange_Exception() {
        List<NETSSTransportQOutDto> transportQOutDtoList = new ArrayList<>();
        transportQOutDtoList.add(new NETSSTransportQOutDto());

        doThrow(new RuntimeException("Exception")).when(netssTransportQOutRepository).saveAll(anyList());
        doThrow(new RuntimeException("Exception")).when(errorHandlingService).dumpBatchToFile(anyList(), anyString(), anyString());

        assertThrows(DataPollException.class, () -> netsstTransportService.saveDataExchange(transportQOutDtoList));

        verify(netssTransportQOutRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testTruncating() {
        netsstTransportService.truncatingData();
        verify(netssTransportQOutRepository, times(1)).truncateTable();
    }
}
