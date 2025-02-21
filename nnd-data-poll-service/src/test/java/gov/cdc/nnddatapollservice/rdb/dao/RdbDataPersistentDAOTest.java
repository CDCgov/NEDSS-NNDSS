package gov.cdc.nnddatapollservice.rdb.dao;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RdbDataPersistentDAOTest {
    @Mock
    private JdbcTemplateUtil jdbcTemplate;



    @InjectMocks
    RdbDataPersistentDAO rdbDataPersistentDAO;

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

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST_TABLE");
        config.setKeyList("key");
        config.setSourceDb("RDB");
        var recordsSaved = rdbDataPersistentDAO.saveRDBData(config, jsondata, true);
        assertNotNull(recordsSaved);
    }

    @Test
    void saveRDBData_CONFIRMATION_METHOD_table() {
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        when(jdbcTemplate.persistingGenericTable(eq(jsondata), any(), eq(true))).thenReturn( new LogResponseModel());

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("CONFIRMATION_METHOD");
        config.setKeyList("key");
        config.setSourceDb("RDB");

        rdbDataPersistentDAO.saveRDBData(config, jsondata, true);
        verify(jdbcTemplate, times(1)).persistingGenericTable(eq(jsondata), any(), eq(true));
    }


}