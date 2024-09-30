package gov.cdc.nnddatapollservice.rdbmodern.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdbmodern.dao.RdbModernDataPersistentDAO;
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
import static gov.cdc.nnddatapollservice.constant.ConstantValue.CRITICAL_NON_NULL_LEVEL;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RdbModernDataHandlingServiceTest {

    @Mock
    private RdbModernDataPersistentDAO rdbModernDataPersistentDAO;
    @Mock
    IPollCommonService pollCommonService;
    @Mock
    IS3DataService is3DataService;
    @InjectMocks
    private RdbModernDataHandlingService rdbModernDataHandlingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rdbModernDataHandlingService.storeInSql = false;
        rdbModernDataHandlingService.storeJsonInS3 = false;
        rdbModernDataHandlingService.pullLimit = 1000;
        rdbModernDataHandlingService.storeJsonInLocalFolder = false;
    }

    @Test
    void handlingExchangedData_initialLoad() throws DataPollException {
        rdbModernDataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST");
        config.setLastUpdateTime(null);
        config.setTableOrder(1);
        config.setQuery("");
        config.setSourceDb("RDB_MODERN");
        configTableList.add(config);

        when(pollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(pollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(pollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(true);

        rdbModernDataHandlingService.handlingExchangedData();
        verify(rdbModernDataPersistentDAO, times(1)).deleteTable(anyString());
        verify(rdbModernDataPersistentDAO, times(1)).saveRdbModernData(any(), any());
    }

    @Test
    void handlingExchangedData_withTimestamp() throws DataPollException {
        rdbModernDataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST");
        config.setLastUpdateTime(Timestamp.from(Instant.now()));
        config.setTableOrder(1);
        config.setQuery("");
        config.setSourceDb("RDB_MODERN");
        configTableList.add(config);

        when(pollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(pollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(pollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(false);

        rdbModernDataHandlingService.handlingExchangedData();
        verify(rdbModernDataPersistentDAO, times(0)).deleteTable(anyString());
        verify(rdbModernDataPersistentDAO, times(1)).saveRdbModernData(any(), any());
    }

    @Test
    void testStoreJsonInS3() throws DataPollException {
        // Arrange
        setupServiceWithMockedDependencies();
        String tableName = "exampleTable";
        rdbModernDataHandlingService.storeJsonInS3= true;
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(1000);
        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, true);

        // Assert
        verify(is3DataService, times(2)).persistToS3MultiPart(anyString(), anyString(), anyString(), any(), anyBoolean());
        verify(pollCommonService, times(2)).updateLastUpdatedTimeAndLogS3(anyString(), any(), anyString());
    }

    @Test
    void testStoreJsonInLocalDir() throws DataPollException {
        // Arrange
        setupServiceWithMockedDependencies();
        String tableName = "exampleTable";
        rdbModernDataHandlingService.storeJsonInLocalFolder= true;
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(1000);
        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, true);

        // Assert
        verify(pollCommonService, times(2)).writeJsonDataToFile(anyString(), anyString(), any(),anyString());
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
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, true);
        // Assert
        verify(pollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
    }

    @Test
    void testPollAndPersistRDBData_exception_FailedTask_2() throws DataPollException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString()))
                .thenReturn(1000);
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString(), anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, true);

        // Assert
        verify(pollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
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
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, true);

        // Assert
        verify(pollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
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

        // Setup rdbModernDataHandlingService flags
        rdbModernDataHandlingService.storeInSql = true;
        rdbModernDataHandlingService.storeJsonInS3 = false;
        rdbModernDataHandlingService.storeJsonInLocalFolder = false;

        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, isInitialLoad);

        // Assert
        verify(pollCommonService, times(1)).getLastUpdatedTime(tableName);
        verify(rdbModernDataPersistentDAO, times(2)).saveRdbModernData(eq(tableName), anyString());
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

        // Setup rdbModernDataHandlingService flags
        rdbModernDataHandlingService.storeInSql = false;
        rdbModernDataHandlingService.storeJsonInS3 = true;
        rdbModernDataHandlingService.storeJsonInLocalFolder = false;

        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, isInitialLoad);

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

        // Setup rdbModernDataHandlingService flags
        rdbModernDataHandlingService.storeInSql = false;
        rdbModernDataHandlingService.storeJsonInS3 = false;
        rdbModernDataHandlingService.storeJsonInLocalFolder = true;

        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, isInitialLoad);

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

        rdbModernDataHandlingService.storeInSql = true;
        rdbModernDataHandlingService.storeJsonInS3 = false;

        // Act
        rdbModernDataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

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

        rdbModernDataHandlingService.storeInSql = false;
        rdbModernDataHandlingService.storeJsonInS3 = true;

        // Act
        rdbModernDataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

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

        rdbModernDataHandlingService.storeInSql = false;
        rdbModernDataHandlingService.storeJsonInS3 = false;

        // Act
        rdbModernDataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, API_LEVEL + log);
        verifyNoMoreInteractions(pollCommonService);
    }


    @Test
    void testUpdateDataHelper_NoExceptionAtApiLevel_StoreInSql() throws DataPollException {
        // Arrange
        boolean exceptionAtApiLevel = false;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        rdbModernDataHandlingService.storeInSql = true;
        rdbModernDataHandlingService.storeJsonInS3 = false;

        when(rdbModernDataPersistentDAO.saveRdbModernData(anyString(), anyString())).thenReturn("Data Saved");

        // Act
        rdbModernDataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(rdbModernDataPersistentDAO, times(1)).saveRdbModernData(tableName, rawJsonData);
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

        rdbModernDataHandlingService.storeInSql = false;
        rdbModernDataHandlingService.storeJsonInS3 = true;

        when(is3DataService.persistToS3MultiPart(anyString(), anyString(), anyString(), any(Timestamp.class), anyBoolean()))
                .thenReturn("S3 Save Success");

        // Act
        rdbModernDataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

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

        rdbModernDataHandlingService.storeInSql = false;
        rdbModernDataHandlingService.storeJsonInS3 = false;

        when(pollCommonService.writeJsonDataToFile(anyString(), anyString(), any(Timestamp.class), anyString())).thenReturn("Local File Save Success");

        // Act
        rdbModernDataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

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

        rdbModernDataHandlingService.storeInSql = false;
        rdbModernDataHandlingService.storeJsonInS3 = true;

        when(is3DataService.persistToS3MultiPart(anyString(), anyString(), anyString(), any(Timestamp.class), anyBoolean()))
                .thenThrow(new RuntimeException("S3 Error"));

        // Act
        rdbModernDataHandlingService.updateDataHelper(exceptionAtApiLevel, tableName, timestamp, rawJsonData, isInitialLoad, log);

        // Assert
        verify(pollCommonService, times(1)).updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, CRITICAL_NON_NULL_LEVEL + "S3 Error");
    }

}