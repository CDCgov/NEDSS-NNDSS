package gov.cdc.nnddatapollservice.rdbmodern.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.constant.ConstantValue;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdbmodern.dto.NrtObservationDto;
import gov.cdc.nnddatapollservice.repository.rdb_modern.NrtObservationRepository;
import gov.cdc.nnddatapollservice.repository.rdb_modern.model.NrtObservation;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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
    private Gson gson;
    @InjectMocks
    private RdbModernDataPersistentDAO rdbModernDataPersistentDAO;

    @Mock
    private HandleError handleError;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
        ConstantValue.SQL_BATCH_SIZE = 10000;
    }

    @Test
    void testPersistingNrtObs_CatchBlock() {
        // Arrange
        NrtObservationDto dto = new NrtObservationDto();
        dto.setObservationUid(12345L);
        List<NrtObservationDto>  nrtObservationList = Collections.singletonList(dto);
        String tableName = "NRT_OBSERVATION";
        RuntimeException mockException = new RuntimeException("Database save error");

        doThrow(mockException).when(nrtObservationRepository).save(any(NrtObservation.class));

        DataPollException exception = assertThrows(DataPollException.class, () ->
                rdbModernDataPersistentDAO.persistingNrtObs(nrtObservationList, tableName));

        assertEquals("Tried individual process, but not success", exception.getMessage());
    }

    @Test
    void testPersistingGenericTable_ExceptionCase_WritesToFile()  {
        // Arrange
        String tableName = "GENERIC_TABLE";
        String jsonData = "[{\"key\": \"value\"}]";
        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);

        doThrow(new RuntimeException("SQL Error")).when(mockSimpleJdbcInsert).executeBatch(any(SqlParameterSource[].class));

        StringBuilder logBuilder = new StringBuilder();

        StringBuilder resultLog = rdbModernDataPersistentDAO.persistingGenericTable(logBuilder, tableName, jsonData);

        assertEquals("Tried individual process, but not success", resultLog.toString());
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

    @Test
    void testSaveRdbModernData_NrtObservation_Success() throws DataPollException {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        String jsonData = "[{\"observation_uid\": 1, \"class_cd\": \"OBS\"}]";

        Type resultType = new TypeToken<List<NrtObservationDto>>() {}.getType();
        List<NrtObservationDto> observationDtos = new ArrayList<>();
        observationDtos.add(new NrtObservationDto());

        when(gson.fromJson(jsonData, resultType)).thenReturn(observationDtos);

        // Act
        rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData);

        // Assert
        verify(nrtObservationRepository, times(1)).save(any(NrtObservation.class));
    }

//    @Test
//    void testSaveRdbModernData_NrtObservation_Exception() throws DataPollException {
//        // Arrange
//        String tableName = "NRT_OBSERVATION";
//        String jsonData = "[{\"observation_uid\": 1, \"class_cd\": \"OBS\"}]";
//
//        Type resultType = new TypeToken<List<NrtObservationDto>>() {}.getType();
//        List<NrtObservationDto> observationDtos = new ArrayList<>();
//        observationDtos.add(new NrtObservationDto());
//
//        when(gson.fromJson(jsonData, resultType)).thenReturn(observationDtos);
//        doThrow(new DataAccessException("Database Error") {}).when(nrtObservationRepository).save(any(NrtObservation.class));
//
//        // Act & Assert
//        rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData);
//
//
//        verify(nrtObservationRepository, times(1)).save(any(NrtObservation.class));
//    }


    @Test
    void testSaveRdbModernData_Else_Success() throws DataPollException {
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
        String result = rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testSaveRdbModernData_Else_Success_2() throws DataPollException {
        // Arrange
        ConstantValue.SQL_BATCH_SIZE = 0;
        String tableName = "SOME_TABLE";
        String jsonData = "[{\"key1\": \"value1\", \"key2\": \"value2\"}]";

        List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);
        assert records != null;

        SimpleJdbcInsert mockSimpleJdbcInsert = mock(SimpleJdbcInsert.class);
        when(mockSimpleJdbcInsert.withTableName(anyString())).thenReturn(mockSimpleJdbcInsert);
        when(mockSimpleJdbcInsert.executeBatch(any(SqlParameterSource[].class))).thenReturn(new int[]{1});

        doNothing().when(jdbcTemplate).execute(anyString());

        // Act
        String result = rdbModernDataPersistentDAO.saveRdbModernData(tableName, jsonData);

        // Assert
        assertNotNull(result);
    }

}