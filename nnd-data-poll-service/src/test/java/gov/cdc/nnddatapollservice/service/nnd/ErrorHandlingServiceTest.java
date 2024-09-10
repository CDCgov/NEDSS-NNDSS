package gov.cdc.nnddatapollservice.service.nnd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.service.nnd.ErrorHandlingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ErrorHandlingServiceTest {

    private ErrorHandlingService errorHandlingService;
    @Mock
    private Logger mockLogger;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        errorHandlingService = new ErrorHandlingService();
        ReflectionTestUtils.setField(errorHandlingService, "logger", mockLogger);
    }

    @Test
    void testDumpBatchToFile_Success() throws IOException {
        List<String> batch = Arrays.asList("item1", "item2", "item3");
        String fileName = "test_batch.json";
        String fileLocation = System.getProperty("java.io.tmpdir"); // Temporary directory for testing

        errorHandlingService.dumpBatchToFile(batch, fileName, fileLocation);

        File file = new File(fileLocation, fileName);
        assertTrue(file.exists());

        String content = Files.readString(file.toPath());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String expectedContent = gson.toJson(batch);
        assertEquals(expectedContent.trim(), content.trim());

        // Clean up
        file.delete();
    }

    @Test
    void testDumpBatchToFile_Exception() {
        List<String> batch = Arrays.asList("item1", "item2", "item3");
        String fileName = "test_batch.json";
        String fileLocation = "/invalid/path";

        errorHandlingService.dumpBatchToFile(batch, fileName, fileLocation);

        verify(mockLogger, atLeastOnce()).error((String) any(), anyString());
    }
}
