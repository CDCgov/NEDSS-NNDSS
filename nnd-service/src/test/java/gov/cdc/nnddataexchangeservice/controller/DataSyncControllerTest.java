package gov.cdc.nnddataexchangeservice.controller;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataSyncControllerTest {

    @Mock
    private IDataExchangeService dataExchangeService;

    @Mock
    private IDataExchangeGenericService dataExchangeGenericService;

    @InjectMocks
    private DataSyncController dataSyncController;


    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }


    @Test
    void exchangingGenericData_WithValidParams_ReturnsBase64CompressedData() throws Exception {
        String tableName = "test_table";
        String timestamp = "2024-07-11";
        String limit = "10";
        String base64CompressedData = "mockBase64Data";
        String load = "true";

        when(dataExchangeGenericService.getDataForDataSync(anyString(), anyString(),anyString(),
                anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(base64CompressedData);

        ResponseEntity<?> response = dataSyncController.dataSync(
                tableName,
                timestamp,
                "0",
                "1",
                limit,
                load,
                "1",
                "false",
                null,
                "",
                null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(base64CompressedData, response.getBody());
    }




    @Test
    void dataSyncTotalRecords_Test() throws Exception {
        String tableName = "test_table";
        String timestamp = "2024-07-11";

        when(dataExchangeGenericService.getTotalRecord(anyString(), anyBoolean(),anyString(), anyBoolean()))
                .thenReturn(10);

        ResponseEntity<?> response = dataSyncController.dataSyncTotalRecords(
                tableName,
                timestamp,
                "true",
                "1",
                "",
                "",
                null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void testDataSyncTotalRecords_whenVersionIsMissing_shouldReturnErrorResponse()  {
        // Arrange
        String tableName = "test_table";
        String timestamp = "2023-01-01T00:00:00";
        String initialLoadApplied = "false";
        String version = ""; // Empty triggers exception
        String useKeyPagination = "false";
        String lastKey = "";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/api/datasync/count/" + tableName);

        // Act
        ResponseEntity<?> response = dataSyncController.dataSyncTotalRecords(
                tableName, timestamp, initialLoadApplied, version,
                useKeyPagination, lastKey, mockRequest
        );

        // Assert
        assertEquals(500, response.getStatusCodeValue()); //NOSONAR

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("gov.cdc.nnddataexchangeservice.exception.DataExchangeException: Version is Missing", body.get("message"));
        assertEquals(500, body.get("status"));
        assertEquals("/api/datasync/count/" + tableName, body.get("path"));
    }

    @Test
    void testDataSync_whenVersionIsMissing_shouldReturnErrorResponse() {
        // Arrange
        String tableName = "test_table";
        String timestamp = "2023-01-01T00:00:00";
        String startRow = "0";
        String endRow = "100";
        String initialLoad = "false";
        String allowNull = "false";
        String version = ""; // <- This triggers the exception
        String noPagination = "false";
        String useKeyPagination = "false";
        String lastKey = "";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/api/datasync/" + tableName);

        // Act
        ResponseEntity<?> response = dataSyncController.dataSync(
                tableName, timestamp, startRow, endRow,
                initialLoad, allowNull, version,
                noPagination, useKeyPagination, lastKey, mockRequest
        );

        // Assert
        assertEquals(500, response.getStatusCodeValue()); // NOSONAR

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Version is Missing", body.get("message"));
        assertEquals(500, body.get("status"));
        assertEquals("/api/datasync/" + tableName, body.get("path"));
    }

    @Test
    void testGetAllTablesCount() throws DataExchangeException {
        String sourceDbName = "testDb";
        String tableName = "testTable";
        String timestamp = "2025-03-27T00:00:00Z";
        String expectedOutput = "{data=[{Table Name=testTable, Record Count=100, Source Database Name=testDb}], message=Success}";

        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("Table Name", tableName);
        mockResult.put("Record Count", 100);
        mockResult.put("Source Database Name", sourceDbName);

        when(dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, false))
                .thenReturn(List.of(mockResult));

        ResponseEntity<?> response = dataSyncController.getAllTablesCount(sourceDbName, tableName,
                timestamp, "", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOutput, Objects.requireNonNull(response.getBody()).toString());
    }

    @Test
    void testGetAllTablesCount_NoResults() throws DataExchangeException {
        String sourceDbName = "testDb";
        String tableName = "testTable";
        String timestamp = "2025-03-27T00:00:00Z";
        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("message", "No results found for the given input(s).");
        expectedOutput.put("data", new ArrayList<>());

        when(dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, false))
                .thenReturn(List.of());

        ResponseEntity<?> response = dataSyncController.getAllTablesCount(sourceDbName, tableName, timestamp, "", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOutput, response.getBody());
    }

    @Test
    void testGetAllTablesCount_ExceptionHandling() throws DataExchangeException {
        String sourceDbName = "testDb";
        String tableName = "testTable";
        String timestamp = "2025-03-27T00:00:00Z";

        when(dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, false))
                .thenThrow(new DataExchangeException("Test exception"));

        ResponseEntity<?> response = dataSyncController.getAllTablesCount(sourceDbName, tableName, timestamp, "", request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
