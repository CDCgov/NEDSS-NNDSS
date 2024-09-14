package gov.cdc.nnddatapollservice.service.rdb.dao;

import gov.cdc.nnddatapollservice.service.rdb.dto.PollDataSyncConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RdbDataPersistentDAOTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Captor
    ArgumentCaptor<SimpleJdbcInsert> simpleJdbcCaptor;

    @InjectMocks
    RdbDataPersistentDAO rdbDataPersistentDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRDBData() {
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        int recordsSaved= rdbDataPersistentDAO.saveRDBData("TEST_TABLE", jsondata);
        assertEquals(0, recordsSaved);
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

        String timetsampResult = rdbDataPersistentDAO.getLastUpdatedTime("TEST", false);
        assertNotNull(timetsampResult);
    }

    @Test
    void updateLastUpdatedTime() {
        Timestamp timestamp = Timestamp.from(Instant.now());

        when(jdbcTemplate.update(anyString(), any(), anyString())).thenReturn(1);
        rdbDataPersistentDAO.updateLastUpdatedTimeAndLog("TEST_TABLE", timestamp, "LOG");

        verify(jdbcTemplate, times(1)).update(anyString(), any(), anyString(), anyString());
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
}