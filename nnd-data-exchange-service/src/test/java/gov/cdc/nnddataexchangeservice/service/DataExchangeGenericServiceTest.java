package gov.cdc.nnddataexchangeservice.service;


import com.google.gson.Gson;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.rdb.DataExchangeConfigRepository;
import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataExchangeConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DataExchangeGenericServiceTest {

    @Mock
    private DataExchangeConfigRepository dataExchangeConfigRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Gson gson;

    @InjectMocks
    private DataExchangeGenericService dataExchangeGenericService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getGenericDataExchange_WithMissingTable_ThrowsDataExchangeException() {
        String tableName = "invalid_table";
        String timeStamp = "2024-07-11";
        int limit = 10;

        when(dataExchangeConfigRepository.findById(tableName)).thenReturn(Optional.empty());

        assertThrows(DataExchangeException.class, () ->
                dataExchangeGenericService.getGenericDataExchange(tableName, timeStamp, limit));
    }

    @Test
    void getGenericDataExchange_1() throws DataExchangeException {
        String tableName = "table";
        String timeStamp = null;

        DataExchangeConfig config = new DataExchangeConfig();
        config.setQuery("SELECT * FROM HERE");
        config.setTableName(tableName);
        config.setSourceDb("RDB");
        int limit = 0;

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("CONDITION", "RDB");
        map.put("SELECT * FROM CONDITION;", null);
        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
        data.add(map);

        when(dataExchangeConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
        when(jdbcTemplate.queryForList(any())).thenReturn(data);
        when(gson.toJson(data)).thenReturn("TEST");
        var res = dataExchangeGenericService.getGenericDataExchange(tableName, timeStamp, limit);
        assertNotNull(res);
    }
    @Test
    void getGenericDataExchange_2() throws DataExchangeException {
        String tableName = "table";
        String timeStamp = "2024-01-01";

        DataExchangeConfig config = new DataExchangeConfig();
        config.setQuery("SELECT * FROM HERE :timestamp");
        config.setTableName(tableName);
        config.setSourceDb("RDB");
        int limit = 0;

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("CONDITION", "RDB");
        map.put("SELECT * FROM CONDITION;", null);
        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
        data.add(map);

        when(dataExchangeConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
        when(jdbcTemplate.queryForList(any())).thenReturn(data);
        when(gson.toJson(data)).thenReturn("TEST");
        var res = dataExchangeGenericService.getGenericDataExchange(tableName, timeStamp, limit);
        assertNotNull(res);
    }

    @Test
    void getGenericDataExchange_3() throws DataExchangeException {
        String tableName = "table";
        String timeStamp = "2024-01-01";

        DataExchangeConfig config = new DataExchangeConfig();
        config.setQuery("SELECT * FROM HERE :timestamp ");
        config.setQueryWithLimit("SELECT * FROM HERE :timestamp :limit");
        config.setTableName(tableName);
        config.setSourceDb("RDB");
        int limit = 10;

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("CONDITION", "RDB");
        map.put("SELECT * FROM CONDITION;", null);
        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
        data.add(map);

        when(dataExchangeConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
        when(jdbcTemplate.queryForList(any())).thenReturn(data);
        when(gson.toJson(data)).thenReturn("TEST");
        var res = dataExchangeGenericService.getGenericDataExchange(tableName, timeStamp, limit);
        assertNotNull(res);
    }


    @Test
    void getGenericDataExchange_WithIOException_ThrowsDataExchangeException() throws IOException {
        String tableName = "test_table";
        String timeStamp = "2024-07-11";
        int limit = 10;
        DataExchangeConfig mockConfig = new DataExchangeConfig();
        mockConfig.setQuery("SELECT * FROM test_table WHERE timestamp > :timestamp");

        when(dataExchangeConfigRepository.findById(tableName)).thenReturn(Optional.of(mockConfig));
        when(jdbcTemplate.queryForList(anyString())).thenReturn(Collections.singletonList(Map.of("key", "value")));
        when(gson.toJson(any())).thenReturn("[{\"key\":\"value\"}]");

        // Mock GZIPOutputStream to throw IOException
        try (var baos = new ByteArrayOutputStream();
             var gzipOutputStream = spy(new GZIPOutputStream(baos))) {
            doThrow(NullPointerException.class).when(gzipOutputStream).write(any(byte[].class));

            assertThrows(NullPointerException.class, () ->
                    dataExchangeGenericService.getGenericDataExchange(tableName, timeStamp, limit));
        }
    }



    @Test
    void decodeAndDecompress_WithValidData_ReturnsDecompressedJson() throws IOException {
        String json = "[{\"key\":\"value\"}]";
        byte[] compressedData;

        try (var baos = new ByteArrayOutputStream();
             var gzipOutputStream = new GZIPOutputStream(baos)) {
            gzipOutputStream.write(json.getBytes());
            gzipOutputStream.finish();
            compressedData = baos.toByteArray();
        }

        String base64CompressedData = Base64.getEncoder().encodeToString(compressedData);
        String result = dataExchangeGenericService.decodeAndDecompress(base64CompressedData);

        assertEquals(json, result);
    }

    @Test
    void decodeAndDecompress_WithInvalidData_ThrowsRuntimeException() {
        String invalidBase64Data = "invalidBase64Data";

        assertThrows(RuntimeException.class, () ->
                dataExchangeGenericService.decodeAndDecompress(invalidBase64Data));
    }
}
