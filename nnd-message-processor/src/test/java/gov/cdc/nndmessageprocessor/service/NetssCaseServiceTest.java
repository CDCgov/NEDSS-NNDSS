package gov.cdc.nndmessageprocessor.service;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.service.interfaces.INETSSTransportQOutService;
import gov.cdc.nndmessageprocessor.service.model.dto.NETSSTransportQOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NetssCaseServiceTest {

    @Mock
    private INETSSTransportQOutService netssTransportQOutService;

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
}
