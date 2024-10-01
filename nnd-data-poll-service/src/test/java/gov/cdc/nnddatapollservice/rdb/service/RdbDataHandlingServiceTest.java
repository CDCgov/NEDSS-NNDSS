package gov.cdc.nnddatapollservice.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RdbDataHandlingServiceTest {

    @Mock
    private RdbDataPersistentDAO rdbDataPersistentDAO;
    @Mock
    IPollCommonService pollCommonService;
    @Mock
    IS3DataService is3DataService;
    @InjectMocks
    private RdbDataHandlingService dataHandlingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataHandlingService.storeJsonInS3 = false;
        dataHandlingService.storeInSql = false;
        dataHandlingService.storeJsonInLocalFolder = false;
        dataHandlingService.pullLimit = 1000;
        dataHandlingService.deleteOnInit = true;
    }

    @Test
    void handlingExchangedData_initialLoad() throws DataPollException {
        dataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_ORGANIZATION");
        config.setLastUpdateTime(null);
        config.setTableOrder(1);
        config.setQuery("");
        config.setSourceDb("RDB");
        configTableList.add(config);

        when(pollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(pollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(pollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(true);

        dataHandlingService.handlingExchangedData();
        verify(rdbDataPersistentDAO, times(1)).deleteTable(anyString());
        verify(rdbDataPersistentDAO, times(1)).saveRDBData(any(), any());
    }

    @Test
    void handlingExchangedData_withTimestamp() throws DataPollException {
        dataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_ORGANIZATION");
        config.setLastUpdateTime(Timestamp.from(Instant.now()));
        config.setTableOrder(1);
        config.setQuery("");
        config.setSourceDb("RDB");
        configTableList.add(config);

        when(pollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(pollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(pollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(false);

        dataHandlingService.handlingExchangedData();
        verify(rdbDataPersistentDAO, times(0)).deleteTable(anyString());
        verify(rdbDataPersistentDAO, times(1)).saveRDBData(any(), any());
    }


    @Test
    void testStoreJsonInS3() throws DataPollException {
        // Arrange
        setupServiceWithMockedDependencies();
        String tableName = "exampleTable";
        dataHandlingService.storeJsonInS3= true;

        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(1000);

        // Act
        dataHandlingService.pollAndPersistRDBData(tableName, true);

        // Assert
        verify(is3DataService, times(2)).persistToS3MultiPart(anyString(), anyString(), anyString(), any(), anyBoolean());
        verify(pollCommonService, times(2)).updateLastUpdatedTimeAndLogS3(anyString(), any(), anyString());


    }

    @Test
    void testStoreJsonInLocalDir() throws DataPollException {
        // Arrange
        setupServiceWithMockedDependencies();
        String tableName = "exampleTable";
        dataHandlingService.storeJsonInLocalFolder= true;

        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(1000);
        // Act
        dataHandlingService.pollAndPersistRDBData(tableName, true);

        // Assert
        verify(pollCommonService, times(2)).writeJsonDataToFile(anyString(), anyString(), any(), anyString());
        verify(pollCommonService, times(2)).updateLastUpdatedTimeAndLogLocalDir(anyString(), any(), anyString());
    }

    private void setupServiceWithMockedDependencies() throws DataPollException {

        when(pollCommonService.decodeAndDecompress(anyString())).thenReturn("{\"data\": \"example\"}");
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2023-01-01T00:00:00Z");
        when(pollCommonService.getLastUpdatedTime(anyString())).thenReturn("2023-01-01T00:00:00Z");
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString(), anyString())).thenReturn("encodedData");



    }

    @Test
    void testPollAndPersistRDBData_exception_task_failed_1() throws DataPollException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        // Act
        dataHandlingService.pollAndPersistRDBData(tableName, true);

        // Assert
        verify(is3DataService, never()).persistToS3MultiPart(anyString(), anyString(), anyString(), any(), anyBoolean());
        verify(rdbDataPersistentDAO, never()).saveRDBData(anyString(), anyString());
        verify(pollCommonService, never()).writeJsonDataToFile(anyString(), anyString(), any(), anyString());
    }

    @Test
    void testPollAndPersistRDBData_exceptionAtApiLevel_FailedTask_2() throws DataPollException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString()))
                .thenReturn(1000);
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString(), anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        // Act
        dataHandlingService.pollAndPersistRDBData(tableName, true);

        // Assert
        verify(is3DataService, never()).persistToS3MultiPart(anyString(), anyString(), anyString(), any(), anyBoolean());
        verify(rdbDataPersistentDAO, never()).saveRDBData(anyString(), anyString());
        verify(pollCommonService, never()).writeJsonDataToFile(anyString(), anyString(), any(), anyString());
    }

    @Test
    void testPollAndPersistRDBData_exceptionAtApiLevel() throws DataPollException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString()))
                .thenReturn(1000);
        when(pollCommonService.callDataExchangeEndpoint(
                eq("testTable"),
                eq(true),
                anyString(),
                eq(false),
                anyString(),
                anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        // Act
        dataHandlingService.pollAndPersistRDBData(tableName, true);

        // Assert
        verify(pollCommonService, times(2)).updateLastUpdatedTimeAndLogLocalDir(eq(tableName), any(), any());
        verify(is3DataService, never()).persistToS3MultiPart(anyString(), anyString(), anyString(), any(), anyBoolean());
        verify(rdbDataPersistentDAO, never()).saveRDBData(anyString(), anyString());
        verify(pollCommonService, never()).writeJsonDataToFile(anyString(), anyString(), any(), anyString());
    }


    @Test
    void testPollAndPersistRDBData_TimeStampSelection_StoreInSql() throws DataPollException {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        boolean isInitialLoad = false;

        // Mocking common service methods
        when(pollCommonService.getLastUpdatedTime(anyString())).thenReturn("2024-10-01 12:00:00");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(100);
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString(), anyString())).thenReturn("mockEncodedData");
        when(pollCommonService.decodeAndDecompress(anyString())).thenReturn("mockRawJsonData");

        // Setup dataHandlingService flags
        dataHandlingService.storeInSql = true;
        dataHandlingService.storeJsonInS3 = false;
        dataHandlingService.storeJsonInLocalFolder = false;

        // Act
        dataHandlingService.pollAndPersistRDBData(tableName, isInitialLoad);

        // Assert
        verify(pollCommonService, times(1)).getLastUpdatedTime(tableName);
        verify(rdbDataPersistentDAO, times(2)).saveRDBData(eq(tableName), anyString());
        verify(pollCommonService, times(2)).updateLastUpdatedTimeAndLog(eq(tableName), any(Timestamp.class), anyString());
    }


    @Test
    void testPollAndPersistRDBData_TimeStampSelection_StoreJsonInS3() throws DataPollException {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        boolean isInitialLoad = false;

        // Mocking common service methods
        when(pollCommonService.getLastUpdatedTimeS3(anyString())).thenReturn("2024-10-01 12:00:00");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(100);
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString(), anyString())).thenReturn("mockEncodedData");
        when(pollCommonService.decodeAndDecompress(anyString())).thenReturn("mockRawJsonData");

        // Setup dataHandlingService flags
        dataHandlingService.storeInSql = false;
        dataHandlingService.storeJsonInS3 = true;
        dataHandlingService.storeJsonInLocalFolder = false;

        // Act
        dataHandlingService.pollAndPersistRDBData(tableName, isInitialLoad);

        // Assert
        verify(pollCommonService, times(1)).getLastUpdatedTimeS3(tableName);
        verify(is3DataService, times(2)).persistToS3MultiPart(eq(RDB), anyString(), eq(tableName), any(Timestamp.class), eq(isInitialLoad));
        verify(pollCommonService, times(2)).updateLastUpdatedTimeAndLogS3(eq(tableName), any(Timestamp.class), anyString());
    }


    @Test
    void testPollAndPersistRDBData_TimeStampSelection_StoreJsonInLocalFolder() throws DataPollException {
        // Arrange
        String tableName = "NRT_OBSERVATION";
        boolean isInitialLoad = false;

        // Mocking common service methods
        when(pollCommonService.getLastUpdatedTimeLocalDir(anyString())).thenReturn("2024-10-01 12:00:00");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(100);
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString(), anyString())).thenReturn("mockEncodedData");
        when(pollCommonService.decodeAndDecompress(anyString())).thenReturn("mockRawJsonData");

        // Setup dataHandlingService flags
        dataHandlingService.storeInSql = false;
        dataHandlingService.storeJsonInS3 = false;
        dataHandlingService.storeJsonInLocalFolder = true;

        // Act
        dataHandlingService.pollAndPersistRDBData(tableName, isInitialLoad);

        // Assert
        verify(pollCommonService, times(1)).getLastUpdatedTimeLocalDir(tableName);
        verify(pollCommonService, times(2)).writeJsonDataToFile(eq(RDB), eq(tableName), any(Timestamp.class), anyString());
        verify(pollCommonService, times(2)).updateLastUpdatedTimeAndLogLocalDir(eq(tableName), any(Timestamp.class), anyString());
    }


    @Test
    void testUpdateDataHelper_ExceptionAtApiLevel_StoreInSql() {
        // Arrange
        boolean exceptionAtApiLevel = true;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        dataHandlingService.storeInSql = true;
        dataHandlingService.storeJsonInS3 = false;

        // Act
        dataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLog(tableName, timestamp, API_LEVEL + log);
        verifyNoMoreInteractions(pollCommonService);
    }

    @Test
    void testUpdateDataHelper_ExceptionAtApiLevel_StoreJsonInS3() {
        // Arrange
        boolean exceptionAtApiLevel = true;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        dataHandlingService.storeInSql = false;
        dataHandlingService.storeJsonInS3 = true;

        // Act
        dataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLogS3(tableName, timestamp, API_LEVEL + log);
        verifyNoMoreInteractions(pollCommonService);
    }

    @Test
    void testUpdateDataHelper_ExceptionAtApiLevel_LocalDir() {
        // Arrange
        boolean exceptionAtApiLevel = true;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        dataHandlingService.storeInSql = false;
        dataHandlingService.storeJsonInS3 = false;

        // Act
        dataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, API_LEVEL + log);
        verifyNoMoreInteractions(pollCommonService);
    }


    @Test
    void testUpdateDataHelper_NoExceptionAtApiLevel_StoreInSql() {
        // Arrange
        boolean exceptionAtApiLevel = false;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        dataHandlingService.storeInSql = true;
        dataHandlingService.storeJsonInS3 = false;

        when(rdbDataPersistentDAO.saveRDBData(anyString(), anyString())).thenReturn("Data Saved");

        // Act
        dataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(rdbDataPersistentDAO, times(1)).saveRDBData(tableName, rawJsonData);
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLog(tableName, timestamp, SQL_LOG + "Data Saved");
    }


    @Test
    void testUpdateDataHelper_NoExceptionAtApiLevel_StoreJsonInS3() {
        // Arrange
        boolean exceptionAtApiLevel = false;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        dataHandlingService.storeInSql = false;
        dataHandlingService.storeJsonInS3 = true;

        when(is3DataService.persistToS3MultiPart(anyString(), anyString(), anyString(), any(Timestamp.class), anyBoolean()))
                .thenReturn("S3 Save Success");

        // Act
        dataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(is3DataService, times(1)).persistToS3MultiPart(RDB, rawJsonData, tableName, timestamp, isInitialLoad);
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLogS3(tableName, timestamp, S3_LOG + "S3 Save Success");
    }


    @Test
    void testUpdateDataHelper_NoExceptionAtApiLevel_LocalDir() {
        // Arrange
        boolean exceptionAtApiLevel = false;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        dataHandlingService.storeInSql = false;
        dataHandlingService.storeJsonInS3 = false;

        when(pollCommonService.writeJsonDataToFile(anyString(), anyString(), any(Timestamp.class), anyString())).thenReturn("Local File Save Success");

        // Act
        dataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(pollCommonService, times(1)).writeJsonDataToFile(RDB, tableName, timestamp, rawJsonData);
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, LOCAL_DIR_LOG + "Local File Save Success");
    }


    @Test
    void testUpdateDataHelper_ExceptionInProcessing() {
        // Arrange
        boolean exceptionAtApiLevel = false;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        dataHandlingService.storeInSql = false;
        dataHandlingService.storeJsonInS3 = true;

        when(is3DataService.persistToS3MultiPart(anyString(), anyString(), anyString(), any(Timestamp.class), anyBoolean()))
                .thenThrow(new RuntimeException("S3 Error"));

        // Act
        dataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, CRITICAL_NON_NULL_LEVEL + "S3 Error");
    }




}