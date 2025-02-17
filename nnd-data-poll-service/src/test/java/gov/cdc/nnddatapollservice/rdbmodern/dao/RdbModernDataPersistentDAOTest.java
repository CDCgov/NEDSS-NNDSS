package gov.cdc.nnddatapollservice.rdbmodern.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationCodedDto;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationDto;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationCodedRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservation;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservationCoded;
import gov.cdc.nnddatapollservice.share.HandleError;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    @Mock
    private NrtObservationRepository nrtObservationRepository;
    @Mock
    private NrtObservationCodedRepository nrtObservationCodedRepository;
    @Mock
    private Gson gson;
    @InjectMocks
    private RdbModernDataPersistentDAO rdbModernDataPersistentDAO;

    @Mock
    private HandleError handleError;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        rdbModernDataPersistentDAO.batchSize = 10000;
    }

    @Test
    void testPersistingGenericTable_ExceptionCase_WritesToFile()  {
        // Arrange
        String tableName = "GENERIC_TABLE";
        String jsonData = "[{\"key\": \"value\"}]";
        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);

        doThrow(new RuntimeException("SQL Error")).when(mockSimpleJdbcInsert).executeBatch(any(SqlParameterSource[].class));

        StringBuilder logBuilder = new StringBuilder();

        StringBuilder resultLog = rdbModernDataPersistentDAO.persistingGenericTable(logBuilder, tableName, jsonData,
                "key", true);
        assertNotNull(resultLog.toString());
    }

    @Test
    void saveRdbModernData(){
        String jsondata = "[{\"CONFIRMATION_METHOD_KEY\":1,\"CONFIRMATION_METHOD_CD\":null,\"CONFIRMATION_METHOD_DESC\":null},\n" +
                "{\"CONFIRMATION_METHOD_KEY\":23,\"CONFIRMATION_METHOD_CD\":\"MR\",\"CONFIRMATION_METHOD_DESC\":\"Medical record review\"}]";
        var recordsSaved = rdbModernDataPersistentDAO.saveRdbModernData("TEST_TABLE", jsondata,
                "key", true);
        assertNotNull(recordsSaved);
    }

    @Test
    void testSaveRdbModernData_withValidData() {
        String tableName = "test_table";
        String jsonData = "[{\"key1\":\"value1\", \"key2\":\"value2\"}]";

        List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
        assert records != null;

        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);
        when(mockSimpleJdbcInsert.withTableName(anyString())).thenReturn(mockSimpleJdbcInsert);
        when(mockSimpleJdbcInsert.executeBatch(any(SqlParameterSource[].class))).thenReturn(new int[]{1});

        doNothing().when(jdbcTemplate).execute(anyString());

        var noOfRecordsSaved = rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData,
                "key", true);

        assertNotNull( noOfRecordsSaved);
    }

    @Test
    void testSaveRdbModernData_withNoData() {
        String tableName = "test_table";
        String jsonData = "[]";

        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);
        when(mockSimpleJdbcInsert.withTableName(anyString())).thenReturn(mockSimpleJdbcInsert);

        doNothing().when(jdbcTemplate).execute(anyString());

        var noOfRecordsSaved = rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData,
                "key", true);

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

    @Test
    void testSaveRdbModernData_NrtObservation_Success() {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        String jsonData = "[{\"observation_uid\": 1, \"class_cd\": \"OBS\"}]";

        Type resultType = new TypeToken<List<NrtObservationDto>>() {}.getType();
        List<NrtObservationDto> observationDtos = new ArrayList<>();
        observationDtos.add(new NrtObservationDto());

        when(gson.fromJson(jsonData, resultType)).thenReturn(observationDtos);

        // Act
        rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData,
                "key", true);

        // Assert
        verify(nrtObservationRepository, times(1)).save(any(NrtObservation.class));
    }

    @Test
    void testSaveRdbModernData_NrtObservationCoded_Success() {
        // Arrange
        String tableName = "nrt_observation_coded";
        String jsonData = "[{\"observation_uid\": 1, \"class_cd\": \"OBS\"}]";

        Type resultType = new TypeToken<List<NrtObservationCodedDto>>() {}.getType();
        List<NrtObservationCodedDto> observationDtos = new ArrayList<>();
        observationDtos.add(new NrtObservationCodedDto());

        when(gson.fromJson(jsonData, resultType)).thenReturn(observationDtos);

        // Act
        rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData,
                "key", true);

        // Assert
        verify(nrtObservationCodedRepository, times(1)).save(any(NrtObservationCoded.class));
    }


    @Test
    void testSaveRdbModernData_Else_Success() {
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
        String result = rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData,
                "key", true);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testSaveRdbModernData_Else_Success_2() {
        // Arrange
        rdbModernDataPersistentDAO.batchSize = 0;
        String tableName = "SOME_TABLE";
        String jsonData = "[{\"key1\": \"value1\", \"key2\": \"value2\"}]";

        List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
        assert records != null;

        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);
        when(mockSimpleJdbcInsert.withTableName(anyString())).thenReturn(mockSimpleJdbcInsert);
        when(mockSimpleJdbcInsert.executeBatch(any(SqlParameterSource[].class))).thenReturn(new int[]{1});

        doNothing().when(jdbcTemplate).execute(anyString());

        // Act
        String result = rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData,
                "key", true);

        // Assert
        assertNotNull(result);
    }

}