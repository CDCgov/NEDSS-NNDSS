package gov.cdc.nnddatapollservice.srte.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.srte.dao.SrteDataPersistentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SrteDataHandlingServiceTest {

    @Mock
    private SrteDataPersistentDAO srteDataPersistentDAO;
    @Mock
    IPollCommonService pollCommonService;
    @Mock
    IS3DataService is3DataService;
    @InjectMocks
    private SrteDataHandlingService srteDataHandlingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        srteDataHandlingService.storeInSql = false;
        srteDataHandlingService.storeJsonInS3 = false;
        srteDataHandlingService.storeJsonInLocalFolder = false;
        srteDataHandlingService.pullLimit = 1000;
    }

    @Test
    void handlingExchangedData_initialLoad() throws DataPollException {
        srteDataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST");
        config.setLastUpdateTime(null);
        config.setTableOrder(1);
        config.setQuery("");
        config.setSourceDb("SRTE");
        configTableList.add(config);

        when(pollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(pollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(pollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(true);

        srteDataHandlingService.handlingExchangedData();
        verify(srteDataPersistentDAO, times(1)).deleteTable(anyString());
        verify(srteDataPersistentDAO, times(1)).saveSRTEData(any(), any());
    }

    @Test
    void testStoreJsonInS3() throws DataPollException {
        // Arrange
        setupServiceWithMockedDependencies();
        String tableName = "exampleTable";
        srteDataHandlingService.storeJsonInS3= true;
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(1000);
        // Act
        srteDataHandlingService.pollAndPersistSRTEData(tableName, true);

        // Assert
        verify(is3DataService, times(2)).persistToS3MultiPart(anyString(), anyString(), anyString(), any(), anyBoolean());
        verify(pollCommonService, times(2)).updateLastUpdatedTimeAndLogS3(anyString(), any(), anyString());
    }

    @Test
    void testStoreJsonInLocalDir() throws DataPollException {
        // Arrange
        setupServiceWithMockedDependencies();
        String tableName = "exampleTable";
        srteDataHandlingService.storeJsonInLocalFolder= true;
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString())).thenReturn(1000);
        // Act
        srteDataHandlingService.pollAndPersistSRTEData(tableName, true);

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
    void testPollAndPersistRDBData_exceptionAtApiLevel_failed_1() throws DataPollException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        // Act
        srteDataHandlingService.pollAndPersistSRTEData(tableName, true);
        // Assert
        verify(pollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
    }

    @Test
    void testPollAndPersistRDBData_exceptionAtApiLevel_failed_2() throws DataPollException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        when(pollCommonService.callDataCountEndpoint(anyString(), anyBoolean(), anyString()))
                .thenReturn(1000);
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString(), anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        // Act
        srteDataHandlingService.pollAndPersistSRTEData(tableName, true);
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
        srteDataHandlingService.pollAndPersistSRTEData(tableName, true);

        // Assert
        verify(pollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
    }
}