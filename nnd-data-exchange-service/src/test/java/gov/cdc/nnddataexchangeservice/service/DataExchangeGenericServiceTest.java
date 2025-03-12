//package gov.cdc.nnddataexchangeservice.service;
//
//
//import com.google.gson.Gson;
//import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
//import gov.cdc.nnddataexchangeservice.repository.rdb.DataSyncConfigRepository;
//import gov.cdc.nnddataexchangeservice.repository.rdb.DataSyncLogRepository;
//import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataSyncConfig;
//import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataSyncLog;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.*;
//import java.util.zip.GZIPOutputStream;
//
//import static gov.cdc.nnddataexchangeservice.constant.DataSyncConstant.DB_RDB_MODERN;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//
//
//class DataExchangeGenericServiceTest {
//
//    @Mock
//    private DataSyncConfigRepository dataSyncConfigRepository;
//    @Mock
//    private DataSyncLogRepository dataSyncLogRepository;
//    @Mock
//    private JdbcTemplate jdbcTemplate;
//
//    @Mock
//    private Gson gson;
//
//    private static final String DB_RDB = "RDB";
//    private static final String DB_SRTE = "SRTE";
//
//    @Mock
//    private JdbcTemplate srteJdbcTemplate;
//    @InjectMocks
//    private DataExchangeGenericService dataExchangeGenericService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//
//    @Test
//    void getGenericDataExchange_WithMissingTable_NoRelevantData() {
//        String tableName = "invalid_table";
//        String timeStamp = "2024-07-11";
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.empty());
//
//        DataExchangeException exception = assertThrows(DataExchangeException.class, () -> {
//            dataExchangeGenericService.getDataForDataSync(tableName, timeStamp, "0",
//                    "1", false, false, false);
//        });
//        assertNotNull(exception);
//
//    }
//
//    @SuppressWarnings("java:S5976")
//    @Test
//    void getGenericDataExchange_1() throws DataExchangeException {
//        String tableName = "table";
//        String timeStamp = null;
//
//        DataSyncConfig config = new DataSyncConfig();
//        config.setQuery("SELECT * FROM HERE");
//        config.setQueryWithPagination("SELECT * FROM HERE");
//        config.setQueryCount("SELECT * FROM HERE");
//        config.setTableName(tableName);
//        config.setSourceDb("RDB");
//        config.setQueryWithNullTimeStamp("TEST SELECT");
//
//        List<Map<String, Object>> data = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("CONDITION", "RDB");
//        map.put("SELECT * FROM CONDITION;", null);
//        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
//        data.add(map);
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
//        when(jdbcTemplate.queryForList(any())).thenReturn(data);
//        when(gson.toJson(data)).thenReturn("TEST");
//        when(dataSyncLogRepository.save(any())).thenReturn(new DataSyncLog());
//        var res = dataExchangeGenericService.getDataForDataSync(tableName, timeStamp,
//                "0", "1", true, false, false);
//        assertNotNull(res);
//    }
//
//    @SuppressWarnings("java:S5976")
//    @Test
//    void getGenericDataExchange_5() throws DataExchangeException {
//        String tableName = "table";
//        String timeStamp = null;
//
//        DataSyncConfig config = new DataSyncConfig();
//        config.setQuery("SELECT * FROM HERE");
//        config.setQueryWithPagination("SELECT * FROM HERE");
//        config.setQueryCount("SELECT * FROM HERE");
//        config.setTableName(tableName);
//        config.setSourceDb("RDB");
//        config.setQueryWithNullTimeStamp("");
//
//        List<Map<String, Object>> data = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("CONDITION", "RDB");
//        map.put("SELECT * FROM CONDITION;", null);
//        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
//        data.add(map);
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
//        when(jdbcTemplate.queryForList(any())).thenReturn(data);
//        when(gson.toJson(data)).thenReturn("TEST");
//        when(dataSyncLogRepository.save(any())).thenReturn(new DataSyncLog());
//        var res = dataExchangeGenericService.getDataForDataSync(tableName, timeStamp, "0",
//                "1", true, false, false);
//        assertNotNull(res);
//    }
//
//    @SuppressWarnings("java:S5976")
//    @Test
//    void getGenericDataExchange_6() throws DataExchangeException {
//        String tableName = "table";
//        String timeStamp = null;
//
//        DataSyncConfig config = new DataSyncConfig();
//        config.setQuery("SELECT * FROM HERE");
//        config.setQueryWithPagination("SELECT * FROM HERE");
//        config.setQueryCount("SELECT * FROM HERE");
//        config.setTableName(tableName);
//        config.setSourceDb("RDB");
//        config.setQueryWithNullTimeStamp(null);
//
//        List<Map<String, Object>> data = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("CONDITION", "RDB");
//        map.put("SELECT * FROM CONDITION;", null);
//        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
//        data.add(map);
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
//        when(jdbcTemplate.queryForList(any())).thenReturn(data);
//        when(gson.toJson(data)).thenReturn("TEST");
//        when(dataSyncLogRepository.save(any())).thenReturn(new DataSyncLog());
//        var res = dataExchangeGenericService.getDataForDataSync(tableName, timeStamp, "0",
//                "1", true, false, false);
//        assertNotNull(res);
//    }
//
//    @SuppressWarnings("java:S5976")
//    @Test
//    void getGenericDataExchange_2() throws DataExchangeException {
//        String tableName = "table";
//        String timeStamp = "2024-01-01";
//
//        DataSyncConfig config = new DataSyncConfig();
//        config.setQuery("SELECT * FROM HERE :timestamp");
//        config.setQueryWithPagination("SELECT * FROM HERE");
//        config.setQueryCount("SELECT * FROM HERE");
//        config.setTableName(tableName);
//        config.setSourceDb("RDB");
//
//        List<Map<String, Object>> data = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("CONDITION", "RDB");
//        map.put("SELECT * FROM CONDITION;", null);
//        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
//        data.add(map);
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
//        when(jdbcTemplate.queryForList(any())).thenReturn(data);
//        when(gson.toJson(data)).thenReturn("TEST");
//        when(dataSyncLogRepository.save(any())).thenReturn(new DataSyncLog());
//        var res = dataExchangeGenericService.getDataForDataSync(tableName, timeStamp, "0",
//                "1", true, false, false);
//        assertNotNull(res);
//    }
//
//    @SuppressWarnings("java:S5976")
//    @Test
//    void getGenericDataExchange_3() throws DataExchangeException {
//        String tableName = "table";
//        String timeStamp = "2024-01-01";
//
//        DataSyncConfig config = new DataSyncConfig();
//        config.setQuery("SELECT * FROM HERE :timestamp ");
//        config.setQueryWithPagination("SELECT * FROM HERE :timestamp :limit");
//        config.setQueryCount("SELECT * FROM HERE");
//        config.setTableName(tableName);
//        config.setSourceDb("RDB");
//
//        List<Map<String, Object>> data = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("CONDITION", "RDB");
//        map.put("SELECT * FROM CONDITION;", null);
//        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
//        data.add(map);
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
//        when(jdbcTemplate.queryForList(any())).thenReturn(data);
//        when(gson.toJson(data)).thenReturn("TEST");
//        when(dataSyncLogRepository.save(any())).thenReturn(new DataSyncLog());
//        var res = dataExchangeGenericService.getDataForDataSync(tableName, timeStamp, "0",
//                "1", false, false, false);
//        assertNotNull(res);
//    }
//
//    @SuppressWarnings("java:S5976")
//    @Test
//    void getGenericDataExchange_4() throws DataExchangeException {
//        String tableName = "table";
//        String timeStamp = "2024-01-01";
//
//        DataSyncConfig config = new DataSyncConfig();
//        config.setQuery("SELECT * FROM HERE :timestamp ");
//        config.setQueryWithPagination("SELECT * FROM HERE :timestamp :limit");
//        config.setQueryCount("SELECT * FROM HERE");
//        config.setQueryWithNullTimeStamp("SELECT * FROM HERE NULL");
//        config.setTableName(tableName);
//        config.setSourceDb("SRTE");
//
//        List<Map<String, Object>> data = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("CONDITION", "RDB");
//        map.put("SELECT * FROM CONDITION;", null);
//        map.put("2024-08-19 15:19:49.4830000", "2024-08-19 15:19:49.4830000");
//        data.add(map);
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(config));
//        when(jdbcTemplate.queryForList(any())).thenReturn(data);
//        when(gson.toJson(data)).thenReturn("TEST");
//        when(dataSyncLogRepository.save(any())).thenReturn(new DataSyncLog());
//        var res = dataExchangeGenericService.getDataForDataSync(tableName, timeStamp, "0",
//                "1", false, false, false);
//        assertNotNull(res);
//    }
//
//    @Test
//    void decodeAndDecompress_WithValidData_ReturnsDecompressedJson() throws DataExchangeException, IOException {
//        String json = "[{\"key\":\"value\"}]";
//        byte[] compressedData;
//
//        try (var baos = new ByteArrayOutputStream();
//             var gzipOutputStream = new GZIPOutputStream(baos)) {
//            gzipOutputStream.write(json.getBytes());
//            gzipOutputStream.finish();
//            compressedData = baos.toByteArray();
//        }
//
//        String base64CompressedData = Base64.getEncoder().encodeToString(compressedData);
//        String result = dataExchangeGenericService.decodeAndDecompress(base64CompressedData);
//
//        assertEquals(json, result);
//    }
//
//    @Test
//    void decodeAndDecompress_WithInvalidData_ThrowsRuntimeException() {
//        String invalidBase64Data = "invalidBase64Data";
//
//        assertThrows(RuntimeException.class, () ->
//                dataExchangeGenericService.decodeAndDecompress(invalidBase64Data));
//    }
//
//
//    @Test
//    void testGetTotalRecord_Success_RDB() throws DataExchangeException {
//        // Arrange
//        String tableName = "NRT_OBSERVATION";
//        boolean initialLoad = false;
//        String timestamp = "2024-10-01 12:00:00";
//        DataSyncConfig dataSyncConfig = new DataSyncConfig();
//        dataSyncConfig.setSourceDb(DB_RDB);
//        dataSyncConfig.setQueryCount("SELECT COUNT(*) FROM NRT_OBSERVATION WHERE last_chg_time :operation :timestamp");
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(dataSyncConfig));
//
//        // Act
//        Integer result = dataExchangeGenericService.getTotalRecord(tableName, initialLoad, timestamp);
//
//        // Assert
//        assertNull( result);
//    }
//
//    @Test
//    void testGetTotalRecord_Success_DB_RDB_MODERN() throws DataExchangeException {
//        // Arrange
//        String tableName = "NRT_OBSERVATION";
//        boolean initialLoad = true;
//        String timestamp = "2024-09-01 10:00:00";
//        String query = "SELECT COUNT(*) FROM NRT_OBSERVATION WHERE last_chg_time < '2024-09-01 10:00:00'";
//        DataSyncConfig dataSyncConfig = new DataSyncConfig();
//        dataSyncConfig.setSourceDb(DB_RDB_MODERN);
//        dataSyncConfig.setQueryCount("SELECT COUNT(*) FROM NRT_OBSERVATION WHERE last_chg_time :operation :timestamp");
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(dataSyncConfig));
//        when(srteJdbcTemplate.queryForObject(query, Integer.class)).thenReturn(150);
//
//        // Act
//        Integer result = dataExchangeGenericService.getTotalRecord(tableName, initialLoad, timestamp);
//
//        // Assert
//        assertNull(result);
//    }
//
//
//    @Test
//    void testGetTotalRecord_Success_SRTE() throws DataExchangeException {
//        // Arrange
//        String tableName = "NRT_OBSERVATION";
//        boolean initialLoad = true;
//        String timestamp = "2024-09-01 10:00:00";
//        String query = "SELECT COUNT(*) FROM NRT_OBSERVATION WHERE last_chg_time < '2024-09-01 10:00:00'";
//        DataSyncConfig dataSyncConfig = new DataSyncConfig();
//        dataSyncConfig.setSourceDb(DB_SRTE);
//        dataSyncConfig.setQueryCount("SELECT COUNT(*) FROM NRT_OBSERVATION WHERE last_chg_time :operation :timestamp");
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(dataSyncConfig));
//        when(srteJdbcTemplate.queryForObject(query, Integer.class)).thenReturn(150);
//
//        // Act
//        Integer result = dataExchangeGenericService.getTotalRecord(tableName, initialLoad, timestamp);
//
//        // Assert
//        assertNull(result);
//    }
//
//    @Test
//    void testGetTotalRecord_Failure_NoTableFound() {
//        // Arrange
//        String tableName = "UNKNOWN_TABLE";
//        boolean initialLoad = false;
//        String timestamp = "2024-10-01 12:00:00";
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(DataExchangeException.class, () -> dataExchangeGenericService.getTotalRecord(tableName, initialLoad, timestamp));
//    }
//
//    @Test
//    void testGetTotalRecord_Failure_UnsupportedDatabase() {
//        // Arrange
//        String tableName = "NRT_OBSERVATION";
//        boolean initialLoad = false;
//        String timestamp = "2024-10-01 12:00:00";
//        DataSyncConfig dataSyncConfig = new DataSyncConfig();
//        dataSyncConfig.setSourceDb("UNSUPPORTED_DB");
//        dataSyncConfig.setQueryCount("SELECT COUNT(*) FROM NRT_OBSERVATION WHERE last_chg_time :operation :timestamp");
//
//        when(dataSyncConfigRepository.findById(tableName)).thenReturn(Optional.of(dataSyncConfig));
//
//        // Act & Assert
//        assertThrows(DataExchangeException.class, () -> dataExchangeGenericService.getTotalRecord(tableName, initialLoad, timestamp));
//    }
//}
