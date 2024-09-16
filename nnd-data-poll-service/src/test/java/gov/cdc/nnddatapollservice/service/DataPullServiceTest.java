package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.rdbmodern.service.interfaces.IRdbModernDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.INNDDataHandlingService;
import gov.cdc.nnddatapollservice.srte.service.interfaces.ISrteDataHandlingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DataPullServiceTest {

    @Mock
    private INNDDataHandlingService dataHandlingService;
    @Mock
    private IRdbDataHandlingService rdbDataHandlingService;
    @Mock
    private IRdbModernDataHandlingService rdbModernDataHandlingService;
    @Mock
    private ISrteDataHandlingService srteDataHandlingService;
    @InjectMocks
    private DataPullService dataPullService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set the cron and zone values using reflection
        ReflectionTestUtils.setField(dataPullService, "cron", "0 0/5 * * * ?");
        ReflectionTestUtils.setField(dataPullService, "zone", "UTC");
        ReflectionTestUtils.setField(dataPullService, "nndPollEnabled", true);
        ReflectionTestUtils.setField(dataPullService, "rdbPollEnabled", true);
        ReflectionTestUtils.setField(dataPullService, "rdbModernPollEnabled", true);
        ReflectionTestUtils.setField(dataPullService, "srtePollEnabled", true);
    }

    @Test
    void testScheduleNNDDataFetch_Success() throws DataPollException {
        dataPullService.scheduleNNDDataFetch();

        verify(dataHandlingService, times(1)).handlingExchangedData();
    }

    @Test
    void testScheduleNNDDataFetch_Exception() throws DataPollException {
        doThrow(new DataPollException("Exception")).when(dataHandlingService).handlingExchangedData();

        assertThrows(DataPollException.class, () -> dataPullService.scheduleNNDDataFetch());

        verify(dataHandlingService, times(1)).handlingExchangedData();
    }
    @Test
    void testScheduleRDBDataFetch_Success() throws DataPollException {
        dataPullService.scheduleRDBDataFetch();
        verify(rdbDataHandlingService, times(1)).handlingExchangedData();
    }
    @Test
    void testScheduleRDBataFetch_Exception() throws DataPollException {
        doThrow(new DataPollException("Exception")).when(rdbDataHandlingService).handlingExchangedData();

        assertThrows(DataPollException.class, () -> dataPullService.scheduleRDBDataFetch());

        verify(rdbDataHandlingService, times(1)).handlingExchangedData();
    }
    @Test
    void testScheduleRDBModernDataFetch_Success() throws DataPollException {
        dataPullService.scheduleRdbModernDataFetch();
        verify(rdbModernDataHandlingService, times(1)).handlingExchangedData();
    }
    @Test
    void testScheduleSrteDataFetch_Success() throws DataPollException {
        dataPullService.scheduleSRTEDataFetch();
        verify(srteDataHandlingService, times(1)).handlingExchangedData();
    }
}