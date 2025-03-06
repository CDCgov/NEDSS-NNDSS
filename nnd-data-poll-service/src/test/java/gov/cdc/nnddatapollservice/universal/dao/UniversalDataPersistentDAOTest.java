package gov.cdc.nnddatapollservice.universal.dao;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationCodedRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationRepository;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.HandleError;
import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UniversalDataPersistentDAOTest {
    @Mock
    private JdbcTemplateUtil jdbcTemplate;
    @Mock
    private DataSource dataSource;
    @Mock
    private NrtObservationRepository nrtObservationRepository;
    @Mock
    private NrtObservationCodedRepository nrtObservationCodedRepository;
    @Mock
    private Gson gson;
    @InjectMocks
    private UniversalDataPersistentDAO universalDataPersistentDAO;

    @Mock
    private HandleError handleError;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        universalDataPersistentDAO.batchSize = 10000;
    }


    @Test
    void saveUniversalData(){
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST_TABLE");
        config.setKeyList("key");


        when(jdbcTemplate.persistingGenericTable(
                jsondata,
                config, true, null)).thenReturn(new LogResponseModel());
        var recordsSaved = universalDataPersistentDAO.saveUniversalData(config, jsondata, true, null);


        assertNotNull(recordsSaved);
    }

}