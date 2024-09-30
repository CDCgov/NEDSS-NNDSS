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

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOCAL_DIR_LOG;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        DataPollException exception = assertThrows(DataPollException.class, () -> {
            dataHandlingService.pollAndPersistRDBData(tableName, true);
        });

        assertNotNull(exception);
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
        DataPollException exception = assertThrows(DataPollException.class, () -> {
            dataHandlingService.pollAndPersistRDBData(tableName, true);
        });
        // Assert
        assertNotNull(exception);
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

}