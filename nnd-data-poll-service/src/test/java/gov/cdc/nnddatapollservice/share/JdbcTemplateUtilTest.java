package gov.cdc.nnddatapollservice.share;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.repository.config.PollDataLogRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationCodedRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationRepository;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JdbcTemplateUtilTest {
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private DataSource dataSource;
    @Mock
    private NrtObservationRepository nrtObservationRepository;
    @Mock
    private NrtObservationCodedRepository nrtObservationCodedRepository;
    @Mock
    private PollDataLogRepository pollDataLogRepository;
    @Mock
    private Gson gson;
    @InjectMocks
    private JdbcTemplateUtil jdbcTemplateUtil;
    private final String tableNameMock = "TEST_TABLE";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final String keyColumn = tableNameMock + "_KEY";
    @Mock
    private SimpleJdbcInsert simpleJdbcInsertMock;

    @Mock
    private HandleError handleError;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        jdbcTemplateUtil.batchSize = 10000;
    }

    @Test
    void testPersistingGenericTable_ExceptionCase_WritesToFile()  {
        // Arrange
        String tableName = "GENERIC_TABLE";
        String jsonData = "[{\"key\": \"value\"}]";
        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);

        doThrow(new RuntimeException("SQL Error")).when(mockSimpleJdbcInsert).executeBatch(any(SqlParameterSource[].class));


        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        var resultLog = jdbcTemplateUtil.persistingGenericTable( jsonData,
                config, true);
        assertNotNull(resultLog);
    }




    @Test
    void testDeleteTable() {
        String tableName = "test_table";

        doNothing().when(jdbcTemplate).execute(anyString());

        jdbcTemplateUtil.deleteTable(tableName);

        verify(jdbcTemplate, times(1)).execute("delete FROM " + tableName);
    }
    @Test
    void deleteTable_shouldLogErrorOnException() {
        String tableName = "non_existing_table";
        doThrow(new RuntimeException("Simulated exception")).when(jdbcTemplate).execute(anyString());
        jdbcTemplateUtil.deleteTable(tableName);
        verify(jdbcTemplate).execute("delete FROM " + tableName);
    }


    @Test
    void getTableListFromConfig() {
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_PROVIDER");
        config.setLastUpdateTime(TimestampUtil.getCurrentTimestamp());
        config.setTableOrder(2);
        config.setQuery("");
        config.setIsSyncEnabled(1);
        configTableList.add(config);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(configTableList);

        List<PollDataSyncConfig> configTableListResult = jdbcTemplateUtil.getTableListFromConfig();
        assertNotNull(configTableListResult);
        assertEquals(1, configTableListResult.size());
    }


    @Test
    void deleteTable() {
        jdbcTemplateUtil.deleteTable("TEST");
        verify(jdbcTemplate, times(1)).execute(anyString());
    }

    @Test
    void getLastUpdatedTime() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("28/08/2024");
        long time = date.getTime();
        Timestamp timetsamp = new Timestamp(time);

        when(jdbcTemplate.queryForObject(
                anyString(), eq(Timestamp.class), anyString())).thenReturn(timetsamp);

        String timetsampResult = jdbcTemplateUtil.getLastUpdatedTime("TEST");
        assertNotNull(timetsampResult);
    }


    @Test
    void updateLastUpdatedTime() {
        Timestamp timestamp = TimestampUtil.getCurrentTimestamp();

        when(jdbcTemplate.update(anyString(), any(), anyString())).thenReturn(1);
        jdbcTemplateUtil.updateLastUpdatedTime("TEST_TABLE", timestamp);

        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyString());
    }

    @Test
    void updateLastUpdatedTimeAndLog_shouldUpdateWithCorrectParameters() {
        String tableName = "my_table";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String log = "Executed successfully";

        // Act
        jdbcTemplateUtil.updateLastUpdatedTimeAndLog(tableName, timestamp, new LogResponseModel());

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(jdbcTemplate).update(sqlCaptor.capture(), paramsCaptor.capture());


        Object[] capturedParams = paramsCaptor.getValue();
        assertEquals(2, capturedParams.length);
        assertEquals(timestamp, capturedParams[0]);
    }



    @Test
    void testGetLastUpdatedTimeS3_Success() {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:34:56");
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
        String expectedFormattedTime = formatter.format(timestamp) + ".000";

        when(jdbcTemplate.queryForObject(
                "select last_update_time_s3 from POLL_DATA_SYNC_CONFIG where table_name=?",
                Timestamp.class, tableName)).thenReturn(timestamp);

        // Act
        String result = jdbcTemplateUtil.getLastUpdatedTimeS3(tableName);

        // Assert
        assertEquals(expectedFormattedTime, result);
    }

    @Test
    void testGetLastUpdatedTimeS3_NullTimestamp() {
        // Arrange
        String tableName = "NRT_OBSERVATION";

        when(jdbcTemplate.queryForObject(
                "select last_update_time_s3 from POLL_DATA_SYNC_CONFIG where table_name=?",
                Timestamp.class, tableName)).thenReturn(null);

        // Act
        String result = jdbcTemplateUtil.getLastUpdatedTimeS3(tableName);

        // Assert
        assertEquals("", result);
    }


    @Test
    void testGetLastUpdatedTimeLocalDir_Success() {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:34:56");
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
        String expectedFormattedTime = formatter.format(timestamp) + ".000";

        when(jdbcTemplate.queryForObject(
                "select last_update_time_local_dir from POLL_DATA_SYNC_CONFIG where table_name=?",
                Timestamp.class, tableName)).thenReturn(timestamp);

        // Act
        String result = jdbcTemplateUtil.getLastUpdatedTimeLocalDir(tableName);

        // Assert
        assertEquals(expectedFormattedTime, result);
    }

    @Test
    void testGetLastUpdatedTimeLocalDir_NullTimestamp() {
        // Arrange
        String tableName = "NRT_OBSERVATION";

        when(jdbcTemplate.queryForObject(
                "select last_update_time_local_dir from POLL_DATA_SYNC_CONFIG where table_name=?",
                Timestamp.class, tableName)).thenReturn(null);

        // Act
        String result = jdbcTemplateUtil.getLastUpdatedTimeLocalDir(tableName);

        // Assert
        assertEquals("", result);
    }


    @Test
    void testUpdateLastUpdatedTimeAndLogS3_Success() {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:34:56");
        String log = "Update successful";

        String expectedSql = "update POLL_DATA_SYNC_CONFIG set last_update_time_s3 =?, last_executed_log=? where table_name=?;";

        // Act
        jdbcTemplateUtil.updateLastUpdatedTimeAndLogS3(tableName, timestamp, new LogResponseModel());

        // Assert
        verify(jdbcTemplate).update(any(), eq(timestamp), eq(tableName));
    }


    @Test
    void testUpdateLastUpdatedTimeAndLogLocalDir_Success() {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:34:56");
        String log = "Local dir update successful";

        String expectedSql = "update POLL_DATA_SYNC_CONFIG set last_update_time_local_dir =?, last_executed_log=? where table_name=?;";

        // Act
        jdbcTemplateUtil.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, new LogResponseModel());

        // Assert
        verify(jdbcTemplate).update(any(), eq(timestamp), eq(tableName));
    }



    @Test
    void testPersistingGenericTable_ExceptionDuringInsert_WritesToFile()  {
        // Arrange
        String tableName = "GENERIC_TABLE";
        String jsonData = "[{\"key\": \"value\"}]";
        List<Map<String, Object>> records = Collections.singletonList(Collections.singletonMap("key", "value"));
        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);

        doThrow(new RuntimeException("Insert Error")).when(mockSimpleJdbcInsert).executeBatch(any(SqlParameterSource[].class));
        doThrow(new RuntimeException("Insert Error")).when(mockSimpleJdbcInsert).execute(any(MapSqlParameterSource.class));

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST_TABLE");
        config.setKeyList("key");
        config.setSourceDb("SRTE");
        // Act & Assert
        jdbcTemplateUtil.persistingGenericTable(jsonData, config, true);

        // Ensure that writing to file is called
        verify(handleError, times(1)).writeRecordToFile(
                any(),
                eq(records.get(0)),
                anyString(),
                any()
        );
    }


    @Test
    void persistingGenericTable_Test()  {
        jdbcTemplateUtil.batchSize = 0;
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST_TABLE");
        config.setKeyList("key");
        config.setSourceDb("SRTE");

        jdbcTemplateUtil.persistingGenericTable(jsondata, config, true);
        verify(handleError, times(2)).writeRecordToFile(
                any(),
                any(),
                anyString(),
                any()
        );
    }

    @Test
    void persistingGenericTable_Test_2()  {
        jdbcTemplateUtil.batchSize = 1000;
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST_TABLE");
        config.setKeyList("key");
        config.setSourceDb("SRTE");

        jdbcTemplateUtil.persistingGenericTable( jsondata, config, true);

        verify(handleError, times(2)).writeRecordToFile(
                any(),
                any(),
                anyString(),
                any()
        );
    }

    // Helper method to create records with keys
    @SuppressWarnings("java:S6213")
    private List<Map<String, Object>> createRecordsWithKeys(Integer... keys) {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Integer key : keys) {
            Map<String, Object> record = new HashMap<>();
            record.put(keyColumn, key);
            records.add(record);
        }
        return records;
    }
}
