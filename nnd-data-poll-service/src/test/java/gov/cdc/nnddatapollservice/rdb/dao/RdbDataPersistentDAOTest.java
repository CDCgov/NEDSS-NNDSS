package gov.cdc.nnddatapollservice.rdb.dao;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.rdb.dto.Condition;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdb.dto.RdbDate;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RdbDataPersistentDAOTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    RdbDataPersistentDAO rdbDataPersistentDAO;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Mock
    private Gson gson;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRDBData() {
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        var recordsSaved = rdbDataPersistentDAO.saveRDBData("TEST_TABLE", jsondata);
        assertNotNull(recordsSaved);
    }

    @Test
    void saveRDBData_CONFIRMATION_METHOD_table() {
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        when(jdbcTemplate.update(anyString())).thenReturn(1);
        rdbDataPersistentDAO.saveRDBData("CONFIRMATION_METHOD", jsondata);
        verify(jdbcTemplate, times(2)).update(anyString());
    }

    @Test
    void saveRDBData_CONDITION_table() {
        String jsondata = "[{\"CONDITION_CD\":null,\"CONDITION_DESC\":null,\"CONDITION_SHORT_NM\":null,\"CONDITION_CD_EFF_DT\":null,\"CONDITION_CD_END_DT\":null," +
                "\"NND_IND\":null,\"CONDITION_KEY\":1,\"DISEASE_GRP_CD\":null,\"DISEASE_GRP_DESC\":null,\"PROGRAM_AREA_CD\":null,\"PROGRAM_AREA_DESC\":null," +
                "\"CONDITION_CD_SYS_CD_NM\":null,\"ASSIGNING_AUTHORITY_CD\":null,\"ASSIGNING_AUTHORITY_DESC\":null,\"CONDITION_CD_SYS_CD\":null}]\n";
        when(jdbcTemplate.update(anyString())).thenReturn(1);
        rdbDataPersistentDAO.saveRDBData("CONDITION", jsondata);
        verify(jdbcTemplate, times(1)).update(anyString());
    }

    @Test
    void saveRDBData_RDB_DATE_table() {
        String jsondata = "[{\"DATE_MM_DD_YYYY\":null,\"DAY_OF_WEEK\":null,\"DAY_NBR_IN_CLNDR_MON\":null,\"DAY_NBR_IN_CLNDR_YR\":null,\"WK_NBR_IN_CLNDR_MON\":null,\"WK_NBR_IN_CLNDR_YR\":null,\"CLNDR_MON_NAME\":null,\"CLNDR_MON_IN_YR\":null,\"CLNDR_QRTR\":null,\"CLNDR_YR\":null,\"DATE_KEY\":1}]";
        when(jdbcTemplate.update(anyString())).thenReturn(1);
        rdbDataPersistentDAO.saveRDBData("RDB_DATE", jsondata);
        verify(jdbcTemplate, times(1)).update(anyString());
    }

    @Test
    void getLastUpdatedTime() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("28/08/2024");
        long time = date.getTime();
        Timestamp timetsamp = new Timestamp(time);

        when(jdbcTemplate.queryForObject(
                anyString(), eq(Timestamp.class), anyString())).thenReturn(timetsamp);

        String timetsampResult = rdbDataPersistentDAO.getLastUpdatedTime("TEST");
        assertNotNull(timetsampResult);
    }

    @Test
    void updateLastUpdatedTime() {
        Timestamp timestamp = Timestamp.from(Instant.now());

        when(jdbcTemplate.update(anyString(), any(), anyString())).thenReturn(1);
        rdbDataPersistentDAO.updateLastUpdatedTime("TEST_TABLE", timestamp);

        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyString());
    }

    @Test
    void getTableListFromConfig() {
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_PROVIDER");
        config.setLastUpdateTime(Timestamp.from(Instant.now()));
        config.setTableOrder(2);
        config.setQuery("");
        configTableList.add(config);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(configTableList);

        List<PollDataSyncConfig> configTableListResult = rdbDataPersistentDAO.getTableListFromConfig();
        assertNotNull(configTableListResult);
        assertEquals(1, configTableListResult.size());
    }

    @Test
    void deleteTable() {
        rdbDataPersistentDAO.deleteTable("TEST");
        verify(jdbcTemplate, times(1)).execute(anyString());
    }

    @Test
    void deleteTable_shouldLogErrorOnException() {
        String tableName = "non_existing_table";
        doThrow(new RuntimeException("Simulated exception")).when(jdbcTemplate).execute(anyString());
        rdbDataPersistentDAO.deleteTable(tableName);
        verify(jdbcTemplate).execute("delete FROM " + tableName);
    }


    @Test
    void updateLastUpdatedTimeAndLog_shouldUpdateWithCorrectParameters() {
        String tableName = "my_table";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String log = "Executed successfully";

        // Act
        rdbDataPersistentDAO.updateLastUpdatedTimeAndLog(tableName, timestamp, log);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(jdbcTemplate).update(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("update RDB.dbo.POLL_DATA_SYNC_CONFIG set last_update_time =?, last_executed_log=? where table_name=?;", sqlCaptor.getValue());

        Object[] capturedParams = paramsCaptor.getValue();
        assertEquals(3, capturedParams.length);
        assertEquals(timestamp, capturedParams[0]);
        assertEquals(log, capturedParams[1]);
        assertEquals(tableName, capturedParams[2]);
    }

    @Test
    void upsertRdbDate_shouldReturnErrorMessageOnException() {
        // Arrange
        RdbDate rdbDate = new RdbDate();
        rdbDate.setDateKey(20230917L);
        String expectedErrorMessage = "Simulated exception";
        doThrow(new RuntimeException(expectedErrorMessage)).when(jdbcTemplate).update(anyString());

        // Act
        String result = rdbDataPersistentDAO.upsertRdbDate(rdbDate);

        // Assert
        assertEquals(expectedErrorMessage, result);
    }

    @Test
    void upsertCondition_shouldReturnErrorMessageOnException() {
        // Arrange
        Condition condition = new Condition();
        condition.setConditionKey(123L);
        String expectedErrorMessage = "Simulated exception";

        doThrow(new RuntimeException(expectedErrorMessage)).when(jdbcTemplate).update(anyString());

        // Act
        String result = rdbDataPersistentDAO.upsertCondition(condition);

        // Assert
        assertEquals(expectedErrorMessage, result);

    }



    @Test
    void testSaveRdbModernData_Else_Success()   {
        // Arrange
        String tableName = "SOME_TABLE";
        String jsonData = "[{\"key1\": \"value1\", \"key2\": \"value2\"}]";

        List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
        assert records != null;

        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);
        when(mockSimpleJdbcInsert.withTableName(anyString())).thenReturn(mockSimpleJdbcInsert);
        when(mockSimpleJdbcInsert.executeBatch(any(SqlParameterSource[].class))).thenReturn(new int[]{1});

        doNothing().when(jdbcTemplate).execute(anyString());

        // Act
        String result = rdbDataPersistentDAO.saveRDBData(tableName, jsonData);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testSaveRdbModernData_Else_Success_2() {
        // Arrange
        rdbDataPersistentDAO.batchSize = 0;
        String tableName = "SOME_TABLE";
        String jsonData = "[{\"key1\": \"value1\", \"key2\": \"value2\"}]";

        List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
        assert records != null;

        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);
        when(mockSimpleJdbcInsert.withTableName(anyString())).thenReturn(mockSimpleJdbcInsert);
        when(mockSimpleJdbcInsert.executeBatch(any(SqlParameterSource[].class))).thenReturn(new int[]{1});

        doNothing().when(jdbcTemplate).execute(anyString());

        // Act
        String result = rdbDataPersistentDAO.saveRDBData(tableName, jsonData);

        // Assert
        assertNotNull(result);
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
        String result = rdbDataPersistentDAO.getLastUpdatedTimeS3(tableName);

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
        String result = rdbDataPersistentDAO.getLastUpdatedTimeS3(tableName);

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
        String result = rdbDataPersistentDAO.getLastUpdatedTimeLocalDir(tableName);

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
        String result = rdbDataPersistentDAO.getLastUpdatedTimeLocalDir(tableName);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testUpdateLastUpdatedTimeAndLogS3_Success() {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:34:56");
        String log = "Update successful";

        String expectedSql = "update RDB.dbo.POLL_DATA_SYNC_CONFIG set last_update_time_s3 =?, last_executed_log=? where table_name=?;";

        // Act
        rdbDataPersistentDAO.updateLastUpdatedTimeAndLogS3(tableName, timestamp, log);

        // Assert
        verify(jdbcTemplate).update(expectedSql, timestamp, log, tableName);
    }

    @Test
    void testUpdateLastUpdatedTimeAndLogLocalDir_Success() {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:34:56");
        String log = "Local dir update successful";

        String expectedSql = "update RDB.dbo.POLL_DATA_SYNC_CONFIG set last_update_time_local_dir =?, last_executed_log=? where table_name=?;";

        // Act
        rdbDataPersistentDAO.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, log);

        // Assert
        verify(jdbcTemplate).update(expectedSql, timestamp, log, tableName);
    }
}