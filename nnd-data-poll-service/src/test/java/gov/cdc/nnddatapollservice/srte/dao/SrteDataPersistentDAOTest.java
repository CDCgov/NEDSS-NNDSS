package gov.cdc.nnddatapollservice.srte.dao;

import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

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
        int recordsSaved = srteDataPersistentDAO.saveSRTEData("TEST_TABLE", jsondata);
        assertEquals(0, recordsSaved);
    }
}