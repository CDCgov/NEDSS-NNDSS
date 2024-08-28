package gov.cdc.nndmessageprocessor.service;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.service.interfaces.INETSSTransportQOutService;
import gov.cdc.nndmessageprocessor.service.model.dto.NETSSTransportQOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NetssCaseServiceTest {

    @Mock
    private INETSSTransportQOutService netssTransportQOutService;
    @Mock
    private File fileMock;
    @Spy
    @InjectMocks
    private NetssCaseService netssCaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(netssCaseService, "fileLocation", System.getProperty("java.io.tmpdir"));
    }

    @Test
    void testGetNetssCases_Success() throws DataProcessorException {
        Short mmwrYear = 2023;
        Short mmwrWeek = 52;
        Boolean includePriorYear = true;

        List<NETSSTransportQOutDto> mockCollection = Arrays.asList(
                new NETSSTransportQOutDto("M012345678901234567890"),
                new NETSSTransportQOutDto("M112345678901234567891")
        );
        when(netssTransportQOutService.getNetssCaseDataYtdAndPriorYear((short) 23, mmwrWeek, (short) 22)).thenReturn(mockCollection);

        doReturn(true).when(netssCaseService).processAndWriteNETSSOutputFile(anyShort(), anyList());

        netssCaseService.getNetssCases(mmwrYear, mmwrWeek, includePriorYear);

        verify(netssTransportQOutService, times(1)).getNetssCaseDataYtdAndPriorYear((short) 23, mmwrWeek, (short) 22);

        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(netssCaseService, times(1)).processAndWriteNETSSOutputFile(eq((short) 23), listCaptor.capture());

        List<NETSSTransportQOutDto> capturedList = listCaptor.getValue();
        assertEquals(1, capturedList.size());
    }

    @Test
    void testGetNetssCases_NoDataFound() throws DataProcessorException {
        Short mmwrYear = 2023;
        Short mmwrWeek = 52;
        Boolean includePriorYear = true;

        when(netssTransportQOutService.getNetssCaseDataYtdAndPriorYear((short) 23, mmwrWeek, (short) 22)).thenReturn(Collections.emptyList());

        assertThrows(DataProcessorException.class, () -> netssCaseService.getNetssCases(mmwrYear, mmwrWeek, includePriorYear));

        verify(netssTransportQOutService, times(1)).getNetssCaseDataYtdAndPriorYear((short) 23, mmwrWeek, (short) 22);
        verify(netssCaseService, never()).processAndWriteNETSSOutputFile(anyShort(), anyList());
    }

    @Test
    void testRemoveDupsFromNetssCollection() {
        List<NETSSTransportQOutDto> mockCollection = Arrays.asList(
                new NETSSTransportQOutDto("M012345678901234567890"),
                new NETSSTransportQOutDto("M012345678901234567890"),
                new NETSSTransportQOutDto("M112345678901234567891")
        );

        List<NETSSTransportQOutDto> result = netssCaseService.removeDupsFromNetssCollection(mockCollection);

        assertEquals(1, result.size());
    }

    @Test
    void testProcessAndWriteNETSSOutputFile_Success() {
        List<NETSSTransportQOutDto> mockCollection = Arrays.asList(
                new NETSSTransportQOutDto("M012345678901234567890"),
                new NETSSTransportQOutDto("M112345678901234567891")
        );

        boolean result = netssCaseService.processAndWriteNETSSOutputFile((short) 23, mockCollection);

        assertTrue(result);
    }

    @Test
    void testWriteNETSSOutputFile_Success() throws IOException {
        List<NETSSTransportQOutDto> mockCollection = Arrays.asList(
                new NETSSTransportQOutDto("M012345678901234567890"),
                new NETSSTransportQOutDto("M112345678901234567891")
        );

        File mockFile = new File(System.getProperty("java.io.tmpdir"), "testfile.dat");

        boolean result = netssCaseService.writeNETSSOutputFile(mockFile, mockCollection, (short) 23);

        assertTrue(result);
    }

    @Test
    void testWriteNETSSOutputFile_Failure() throws IOException {
        List<NETSSTransportQOutDto> mockCollection = Collections.emptyList();

        File mockFile = new File(System.getProperty("java.io.tmpdir"), "testfile.dat");

        boolean result = netssCaseService.writeNETSSOutputFile(mockFile, mockCollection, (short) 23);

        assertFalse(result);
    }

    @Test
    public void testGetNetssCasesYearLessThan2000() throws DataProcessorException {
        // Set up the inputs for the test
        Short mmwrYear = 1999;
        Short mmwrWeek = 10;
        Boolean includePriorYear = false;

        // Mock the method calls
        when(netssCaseService.getNETSSTransportQOutDTCollectionForYear(anyShort(), anyShort(), anyBoolean()))
                .thenReturn(List.of(new NETSSTransportQOutDto())); // returning a non-empty collection to avoid the exception

        when(netssCaseService.processAndWriteNETSSOutputFile(anyShort(), anyList()))
                .thenReturn(true); // simulate a successful write

        // Call the method under test
        netssCaseService.getNetssCases(mmwrYear, mmwrWeek, includePriorYear);

        // Verify the behavior: Year should not be modified (no subtraction)
        verify(netssCaseService, times(1)).getNETSSTransportQOutDTCollectionForYear(eq(mmwrYear), eq(mmwrWeek), eq(includePriorYear));
    }

    @Test
    public void testGetNetssCasesWrittenFalse() throws DataProcessorException {
        // Set up the inputs for the test
        Short mmwrYear = 2023;
        Short mmwrWeek = 10;
        Boolean includePriorYear = false;

        // Mock the method calls
        when(netssCaseService.getNETSSTransportQOutDTCollectionForYear(anyShort(), anyShort(), anyBoolean()))
                .thenReturn(List.of(new NETSSTransportQOutDto())); // returning a non-empty collection to avoid the exception

        when(netssCaseService.processAndWriteNETSSOutputFile(anyShort(), anyList()))
                .thenReturn(false); // simulate a failed write

        // Call the method under test
        netssCaseService.getNetssCases(mmwrYear, mmwrWeek, includePriorYear);

        // Verify that the correct log message is produced
        verify(netssCaseService, times(1)).processAndWriteNETSSOutputFile(eq((short) 23), anyList());
    }

    @Test
    public void testGetNetssCasesNoDataFound() throws DataProcessorException {
        // Set up the inputs for the test
        Short mmwrYear = 2023;
        Short mmwrWeek = 10;
        Boolean includePriorYear = false;

        // Mock the method to return an empty collection
        when(netssCaseService.getNETSSTransportQOutDTCollectionForYear(anyShort(), anyShort(), anyBoolean()))
                .thenReturn(Collections.emptyList()); // returning an empty collection to simulate "No Data Found"

        // Expect DataProcessorException to be thrown
        assertThrows(DataProcessorException.class, () -> {
            netssCaseService.getNetssCases(mmwrYear, mmwrWeek, includePriorYear);
        });

        // Verify that processAndWriteNETSSOutputFile is never called due to the exception
        verify(netssCaseService, never()).processAndWriteNETSSOutputFile(anyShort(), anyList());
    }

    @Test
    public void testRemoveDupsFromNetssCollectionWithNullPayload() {
        // Create a list with one item having null payload
        List<NETSSTransportQOutDto> inputList = new ArrayList<>();
        inputList.add(new NETSSTransportQOutDto());  // Assuming a constructor for testing purposes

        // Call the method under test
        List<NETSSTransportQOutDto> result = netssCaseService.removeDupsFromNetssCollection(inputList);

        // Verify the result - the list should be empty since the payload is null
        assertEquals(0, result.size());
    }

    @Test
    public void testRemoveDupsFromNetssCollectionWithShortPayload() {
        // Create a list with one item having a payload length < 12
        List<NETSSTransportQOutDto> inputList = new ArrayList<>();
        inputList.add(new NETSSTransportQOutDto("12345"));  // Payload length < 12

        // Call the method under test
        List<NETSSTransportQOutDto> result = netssCaseService.removeDupsFromNetssCollection(inputList);

        // Verify the result - the list should be empty since the payload length is < 12
        assertEquals(0, result.size());
    }

    @Test
    public void testRemoveDupsFromNetssCollectionWithValidPayloads() {
        // Create a list with valid payloads
        List<NETSSTransportQOutDto> inputList = new ArrayList<>();
        inputList.add(new NETSSTransportQOutDto("ABC123456789"));  // Valid payload
        inputList.add(new NETSSTransportQOutDto("XYZ123456789"));  // Same CASE REPORT ID (456789) as above

        // Call the method under test
        List<NETSSTransportQOutDto> result = netssCaseService.removeDupsFromNetssCollection(inputList);

        // Verify the result - there should be only 1 entry as they have the same CASE REPORT ID
        assertEquals(1, result.size());
    }



    @Test
    public void testProcessAndWriteNETSSOutputFileDirectoryCreationFailure() {
        // Simulate the directory does not exist and cannot be created
        when(fileMock.exists()).thenReturn(false);
        when(fileMock.mkdirs()).thenReturn(false);

        // Test the method
        boolean result = netssCaseService.processAndWriteNETSSOutputFile((short) 2023, Collections.emptyList());

        // Verify that the result is false due to directory creation failure
        assertFalse(result);
    }

    @Test
    public void testProcessAndWriteNETSSOutputFileNotADirectory() {
        // Simulate the directory exists but is not a directory
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isDirectory()).thenReturn(false);

        // Test the method
        boolean result = netssCaseService.processAndWriteNETSSOutputFile((short) 2023, Collections.emptyList());

        // Verify that the result is false because the path is not a directory
        assertFalse(result);
    }


}
