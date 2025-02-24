package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.INNDDataHandlingService;
import gov.cdc.nnddatapollservice.universal.service.interfaces.IUniversalDataHandlingService;
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
    private IUniversalDataHandlingService universalDataHandlingService;
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
        verify(universalDataHandlingService, times(1)).handlingExchangedData("RDB");
    }
    @Test
    void testScheduleRDBataFetch_Exception() throws DataPollException {
        doThrow(new DataPollException("Exception")).when(universalDataHandlingService).handlingExchangedData("RDB");

        assertThrows(DataPollException.class, () -> dataPullService.scheduleRDBDataFetch());

        verify(universalDataHandlingService, times(1)).handlingExchangedData("RDB");
    }
    @Test
    void testScheduleRDBModernDataFetch_Success() throws DataPollException {
        dataPullService.scheduleRdbModernDataFetch();
        verify(universalDataHandlingService, times(1)).handlingExchangedData(any());
    }
    @Test
    void testScheduleSrteDataFetch_Success() throws DataPollException {
        dataPullService.scheduleSRTEDataFetch();
        verify(universalDataHandlingService, times(1)).handlingExchangedData("SRTE");
    }
}