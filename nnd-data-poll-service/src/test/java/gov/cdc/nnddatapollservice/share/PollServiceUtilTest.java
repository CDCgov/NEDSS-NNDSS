package gov.cdc.nnddatapollservice.share;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

class PollServiceUtilTest {

    private static final String LOCAL_FILE_PATH = "/tmp";
    private static final String DB_SOURCE = "testdb";
    private static final String TABLE_NAME = "testtable";
    private static final Timestamp TIME_STAMP = Timestamp.valueOf("2024-09-17 00:00:00");
    private static final String JSON_DATA = "{\"key\":\"value\"}";
    private Logger logger = LoggerFactory.getLogger(PollServiceUtil.class); //NOSONAR

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
    }

    @Test
    void testWriteJsonToFileSuccess() {
        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
            filesMock.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            filesMock.when(() -> Files.writeString(any(Path.class), eq(JSON_DATA), eq(StandardOpenOption.CREATE)))
                    .thenReturn(null);

            String result = PollServiceUtil.writeJsonToFile(LOCAL_FILE_PATH, DB_SOURCE, TABLE_NAME, TIME_STAMP, JSON_DATA);

            filesMock.verify(() -> Files.createDirectories(any(Path.class)));
            filesMock.verify(() -> Files.writeString(any(Path.class), eq(JSON_DATA), eq(StandardOpenOption.CREATE)));

            assertEquals(LOG_SUCCESS, result);
        }
    }

    @Test
    void testWriteJsonToFileFailure() {
        String expectedErrorMessage = "Simulated Exception";

        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
            filesMock.when(() -> Files.createDirectories(any(Path.class)))
                    .thenThrow(new RuntimeException(expectedErrorMessage));

            String result = PollServiceUtil.writeJsonToFile(LOCAL_FILE_PATH, DB_SOURCE, TABLE_NAME, TIME_STAMP, JSON_DATA);

            assertEquals(expectedErrorMessage, result);
            filesMock.verify(() -> Files.createDirectories(any(Path.class)));
        }
    }
}