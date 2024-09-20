package gov.cdc.nnddatapollservice.srte.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SrteDataPersistentDAOTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SrteDataPersistentDAO srteDataPersistentDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        verify(jdbcTemplate).execute("delete " + tableName);
    }
}