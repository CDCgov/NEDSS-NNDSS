package gov.cdc.nnddatapollservice.rdbmodern.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RdbModernDataPersistentDAOTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RdbModernDataPersistentDAO rdbModernDataPersistentDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRdbModernData() {
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        int recordsSaved = rdbModernDataPersistentDAO.saveRdbModernData("TEST_TABLE", jsondata);
        assertEquals(0, recordsSaved);
    }
}