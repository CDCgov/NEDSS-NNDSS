package gov.cdc.nnddatapollservice.rdbmodern.dao;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RdbModernDataPersistentDAOTest {
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private DataSource dataSource;

    @InjectMocks
    private RdbModernDataPersistentDAO rdbModernDataPersistentDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);

    }

    @Test
    void saveRdbModernData() throws DataPollException {
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        var recordsSaved = rdbModernDataPersistentDAO.saveRdbModernData("TEST_TABLE", jsondata);
        assertNotNull(recordsSaved);
    }

    @Test
    void testSaveRdbModernData_withValidData() throws DataPollException {
        String tableName = "test_table";
        String jsonData = "[{\"key1\":\"value1\", \"key2\":\"value2\"}]";

        List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
        assert records != null;

        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);
        when(mockSimpleJdbcInsert.withTableName(anyString())).thenReturn(mockSimpleJdbcInsert);
        when(mockSimpleJdbcInsert.executeBatch(any(SqlParameterSource[].class))).thenReturn(new int[]{1});

        doNothing().when(jdbcTemplate).execute(anyString());

        var noOfRecordsSaved = rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData);

        verify(jdbcTemplate, times(1)).execute(anyString());

        assertNotNull( noOfRecordsSaved);
    }

    @Test
    void testSaveRdbModernData_withNoData() throws DataPollException {
        String tableName = "test_table";
        String jsonData = "[]";

        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);
        when(mockSimpleJdbcInsert.withTableName(anyString())).thenReturn(mockSimpleJdbcInsert);

        doNothing().when(jdbcTemplate).execute(anyString());

        var noOfRecordsSaved = rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData);

        verify(jdbcTemplate, times(1)).execute(anyString());

        assertEquals("SUCCESS", noOfRecordsSaved);
    }

    @Test
    void testDeleteTable() {
        String tableName = "test_table";

        doNothing().when(jdbcTemplate).execute(anyString());

        rdbModernDataPersistentDAO.deleteTable(tableName);

        verify(jdbcTemplate, times(1)).execute("delete FROM " + tableName);
    }
    @Test
    void deleteTable_shouldLogErrorOnException() {
        String tableName = "non_existing_table";
        doThrow(new RuntimeException("Simulated exception")).when(jdbcTemplate).execute(anyString());
        rdbModernDataPersistentDAO.deleteTable(tableName);
        verify(jdbcTemplate).execute("delete FROM " + tableName);
    }
}