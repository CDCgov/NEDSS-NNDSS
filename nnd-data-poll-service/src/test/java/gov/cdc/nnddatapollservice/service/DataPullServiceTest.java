package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.IDataHandlingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DataPullServiceTest {

    @Mock
    private IDataHandlingService dataHandlingService;

    @InjectMocks
    private DataPullService dataPullService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set the cron and zone values using reflection
        ReflectionTestUtils.setField(dataPullService, "cron", "0 0/5 * * * ?");
        ReflectionTestUtils.setField(dataPullService, "zone", "UTC");
    }

    @Test
    void testScheduleDataFetch_Success() throws DataPollException {
        dataPullService.scheduleNNDDataFetch();

        verify(dataHandlingService, times(1)).handlingExchangedData();
    }

    @Test
    void testScheduleDataFetch_Exception() throws DataPollException {
        doThrow(new DataPollException("Exception")).when(dataHandlingService).handlingExchangedData();

        assertThrows(DataPollException.class, () -> dataPullService.scheduleNNDDataFetch());

        verify(dataHandlingService, times(1)).handlingExchangedData();
    }
}