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
        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, true);

        // Assert
        verify(is3DataService).persistToS3MultiPart(anyString(), anyString(), anyString(), any(), anyBoolean());
        verify(pollCommonService).updateLastUpdatedTimeAndLog(anyString(), any(), anyString());
    }

    @Test
    void testStoreJsonInLocalDir() throws DataPollException {
        // Arrange
        setupServiceWithMockedDependencies();
        String tableName = "exampleTable";
        rdbModernDataHandlingService.storeJsonInLocalFolder= true;
        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, true);

        // Assert
        verify(pollCommonService).writeJsonDataToFile(anyString(), anyString(), any(),anyString());
        verify(pollCommonService).updateLastUpdatedTimeAndLog(anyString(), any(), anyString());
    }

    private void setupServiceWithMockedDependencies() throws DataPollException {

        when(pollCommonService.decodeAndDecompress(anyString())).thenReturn("{\"data\": \"example\"}");
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2023-01-01T00:00:00Z");
        when(pollCommonService.getLastUpdatedTime(anyString())).thenReturn("2023-01-01T00:00:00Z");
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString())).thenReturn("encodedData");



    }

    @Test
    void testPollAndPersistRDBData_exceptionAtApiLevel() throws DataPollException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(pollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        when(pollCommonService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        // Act
        rdbModernDataHandlingService.pollAndPersistRDBMOdernData(tableName, true);

        // Assert
        verify(pollCommonService).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
    }
}