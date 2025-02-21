package gov.cdc.nnddatapollservice.srte.dao;

import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.share.HandleError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST_TABLE");
        config.setKeyList("key");
        config.setSourceDb("SRTE");
        var recordsSaved = srteDataPersistentDAO.saveSRTEData(config, jsondata, true);
        assertNotNull( recordsSaved);
    }
    @Test
    void saveSRTEData_with_zero_record() {
        String jsondata = "[]";

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST_TABLE");
        config.setKeyList("key");
        config.setSourceDb("SRTE");
        var recordsSaved = srteDataPersistentDAO.saveSRTEData(config, jsondata, true);
        assertNotNull( recordsSaved);
    }



}