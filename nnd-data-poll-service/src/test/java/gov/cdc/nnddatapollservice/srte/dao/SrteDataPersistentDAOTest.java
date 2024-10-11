package gov.cdc.nnddatapollservice.srte.dao;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.share.HandleError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SrteDataPersistentDAOTest {
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private HandleError handleError;

    @InjectMocks
    private SrteDataPersistentDAO srteDataPersistentDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        srteDataPersistentDAO.batchSize = 1000;
    }

    @Test
    void saveSRTEData() {
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        var recordsSaved = srteDataPersistentDAO.saveSRTEData("TEST_TABLE", jsondata);
        assertNotNull( recordsSaved);
    }
    @Test
    void saveSRTEData_with_zero_record() {
        String jsondata = "[]";
        var recordsSaved = srteDataPersistentDAO.saveSRTEData("TEST_TABLE", jsondata);
        assertNotNull( recordsSaved);
    }
    @Test
    void deleteTable() {
        srteDataPersistentDAO.deleteTable("TEST");
        verify(jdbcTemplate, times(1)).execute(anyString());
    }
    @Test
    void deleteTable_shouldLogErrorOnException() {
        String tableName = "non_existing_table";
        doThrow(new RuntimeException("Simulated exception")).when(jdbcTemplate).execute(anyString());
        srteDataPersistentDAO.deleteTable(tableName);
        verify(jdbcTemplate).execute("delete FROM " + tableName);
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

        // Act & Assert
        srteDataPersistentDAO.persistingGenericTable(tableName, jsonData);

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
        srteDataPersistentDAO.batchSize = 0;
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        srteDataPersistentDAO.persistingGenericTable("TEST_TABLE", jsondata);
        verify(handleError, times(2)).writeRecordToFile(
                any(),
                any(),
                anyString(),
                any()
        );
    }

    @Test
    void persistingGenericTable_Test_2()  {
        srteDataPersistentDAO.batchSize = 1000;
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        srteDataPersistentDAO.persistingGenericTable("TEST_TABLE", jsondata);

        verify(handleError, times(2)).writeRecordToFile(
                any(),
                any(),
                anyString(),
                any()
        );
    }
}