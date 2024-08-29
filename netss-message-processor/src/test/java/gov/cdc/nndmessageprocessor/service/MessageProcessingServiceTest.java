package gov.cdc.nndmessageprocessor.service;


import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.service.interfaces.INetssCaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MessageProcessingServiceTest {

    @Mock
    private INetssCaseService netssCaseService;

    @InjectMocks
    private MessageProcessingService messageProcessingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(messageProcessingService, "date", "12/30/2023");
        ReflectionTestUtils.setField(messageProcessingService, "prior", true);
    }

    @Test
    void testScheduleDataFetch_Success() throws DataProcessorException {
        messageProcessingService.scheduleDataFetch();
        verify(netssCaseService, times(1)).getNetssCases((short) 2023, (short) 52, true);
    }

    @Test
    void testProcessDateInput_Success() throws DataProcessorException {
        Map<String, Short> dateData = messageProcessingService.processDateInput("12/30/2023");
        assertEquals((short) 52, dateData.get("WEEK"));
        assertEquals((short) 2023, dateData.get("YEAR"));
    }

    @Test
    void testProcessDateInput_Exception() {
        assertThrows(DataProcessorException.class, () -> {
            messageProcessingService.processDateInput("invalid_date");
        });
    }

    @Test
    void testGetMMWRAndPriorYear() {
        Date specifiedDate = new Date();
        try {
            specifiedDate = new SimpleDateFormat("MM/dd/yyyy").parse("12/30/2023");
        } catch (Exception e) {
            // Ignore for testing
        }

        Map<String, Short> result = ReflectionTestUtils.invokeMethod(
                messageProcessingService, "getMMWRAndPriorYear", specifiedDate);

        assertEquals((short) 52, result.get("WEEK"));
        assertEquals((short) 2023, result.get("YEAR"));
    }

    @Test
    void testCalcMMWR() {
        int[] result = ReflectionTestUtils.invokeMethod(
                messageProcessingService, "calcMMWR", "12/30/2023");

        assertEquals(52, result[0]);
        assertEquals(2023, result[1]);
    }

    @Test
    void testAdjustYearForWeekZero_StartOfNewYear() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1); // New Year's Day
        long t = cal.getTimeInMillis();
        int y = 2023;

        // Act
        int result = messageProcessingService.adjustYearForWeekZero(t, y);

        // Assert
        assertEquals(52, result, "Expected MMWR week should be the last week of the previous year.");
    }

    @Test
    void testAdjustYearForWeekZero_NotStartOfNewYear() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1); // Not the start of the year
        long t = cal.getTimeInMillis();
        int y = 2023;

        // Act
        int result = messageProcessingService.adjustYearForWeekZero(t, y);

        // Assert
        assertEquals(2023, result, "Expected year should remain unchanged.");
    }

    @Test
    void testAdjustYearForWeekZero_EdgeCase() {
        // Arrange
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 5); // Edge case, within the first week
        long t = cal.getTimeInMillis();
        int y = 2023;

        // Act
        int result = messageProcessingService.adjustYearForWeekZero(t, y);

        // Assert
        assertEquals(52, result, "Expected MMWR week should be the last week of the previous year.");
    }

    @Test
    void testCalcMMWR_Week53_TransitionToNewYear() throws Exception {
        // Arrange
        String pDate = "12/29/2018"; // This should fall in the 53rd week of 2018
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = dateFormat.parse(pDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // Act
        int[] result = messageProcessingService.calcMMWR(pDate);

        // Assert
        assertEquals(52, result[0], "Week should transition to 1");
        assertEquals(2018, result[1], "Year should transition to 2019");
    }

    @Test
    void testCalcMMWR_WeekZero_AdjustToPreviousYear() throws Exception {
        // Arrange
        String pDate = "01/01/2019"; // This should adjust to the last week of 2018
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = dateFormat.parse(pDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // Act
        int[] result = messageProcessingService.calcMMWR(pDate);

        // Assert
        assertEquals(1, result[0], "Week should adjust to the last week of 2018");
        assertEquals(2019, result[1], "Year should adjust to 2018");
    }

    @Test
    void testCalcMMWR_ExceptionHandling() {
        // Arrange
        String pDate = "invalid-date"; // This should trigger an exception

        // Act
        int[] result = messageProcessingService.calcMMWR(pDate);

        // Assert
        assertEquals(0, result[0], "Week should be 0 due to exception");
        assertEquals(0, result[1], "Year should be 0 due to exception");
    }
}
