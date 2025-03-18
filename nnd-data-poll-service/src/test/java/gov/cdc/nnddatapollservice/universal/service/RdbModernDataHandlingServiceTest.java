package gov.cdc.nnddatapollservice.universal.service;

import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.IApiService;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.TimestampUtil;
import gov.cdc.nnddatapollservice.universal.dao.UniversalDataPersistentDAO;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.RDB;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RdbModernDataHandlingServiceTest {

    @Mock
    private UniversalDataPersistentDAO universalDataPersistentDAO;
    @Mock
    IPollCommonService iPollCommonService;
    @Mock
    IApiService iApiService;
    @Mock
    IS3DataService is3DataService;
    @InjectMocks
    private UniversalDataHandlingService universalDataHandlingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        universalDataHandlingService.storeInSql = false;
        universalDataHandlingService.storeJsonInS3 = false;
        universalDataHandlingService.pullLimit = 1000;
        universalDataHandlingService.storeJsonInLocalFolder = false;
        universalDataHandlingService.deleteOnInit = true;
    }

    @Test
    void handlingExchangedData_initialLoad() throws DataPollException, APIException {
        universalDataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST");
        config.setLastUpdateTime(null);
        config.setTableOrder(1);
        config.setSourceDb("RDB_MODERN");
        config.setIsSyncEnabled(1);
        configTableList.add(config);

        when(iPollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(iPollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(iPollCommonService.filterSyncEnabledTables(anyList())).thenReturn(configTableList);
        when(iPollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(true);

        universalDataHandlingService.handlingExchangedData("RDB");
        verify(iPollCommonService, times(1)).deleteTable(anyString());
    }

    @Test
    void handlingExchangedData_withTimestamp() throws DataPollException, APIException {
        universalDataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST");
        config.setLastUpdateTime(TimestampUtil.getCurrentTimestamp());
        config.setTableOrder(1);
        config.setSourceDb("RDB_MODERN");
        config.setIsSyncEnabled(1);
        configTableList.add(config);

        when(iPollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(iPollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(iPollCommonService.filterSyncEnabledTables(anyList())).thenReturn(configTableList);
        when(iPollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(false);

        universalDataHandlingService.handlingExchangedData("RDB");
        verify(iPollCommonService, times(0)).deleteTable(anyString());
    }

    @Test
    void testStoreJsonInLocalDir() throws DataPollException, APIException {
        // Arrange
        setupServiceWithMockedDependencies();
        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder= true;

        var apiModel = new ApiResponseModel<Integer>();
        apiModel.setResponse(1000);
        when(iApiService.callDataCountEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString()))
                .thenReturn(apiModel);

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.pollAndPersistData(true, config);

        // Assert
        verify(iPollCommonService, times(1)).writeJsonDataToFile(anyString(), anyString(),
                any(),anyString(), any());
//        verify(iPollCommonService, times(1)).updateLastUpdatedTimeAndLogLocalDir(anyString(), any(), any());
    }

    ApiResponseModel shareResString = new ApiResponseModel<String>();
    private void setupServiceWithMockedDependencies() throws DataPollException {

        when(iPollCommonService.decodeAndDecompress(anyString())).thenReturn("{\"data\": \"example\"}");
        when(iPollCommonService.getCurrentTimestamp()).thenReturn("2023-01-01T00:00:00Z");
        when(iPollCommonService.getLastUpdatedTime(anyString())).thenReturn("2023-01-01T00:00:00Z");

        shareResString.setResponse("encodedData");
        when(iApiService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(),
                anyBoolean(), anyString(), anyString(), anyBoolean(), anyBoolean(),
                anyString())).thenReturn(shareResString);



    }

    @Test
    void testPollAndPersistRDBData_exception_task_failed_1() throws DataPollException, APIException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(iPollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        when(iApiService.callDataCountEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.pollAndPersistData( true, config);
        // Assert
        verify(iPollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
    }

    @Test
    void testPollAndPersistRDBData_exception_FailedTask_2() throws DataPollException, APIException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(iPollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        var apiModel = new ApiResponseModel<Integer>();
        apiModel.setResponse(1000);
        when(iApiService.callDataCountEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString()))
                .thenReturn(apiModel);
        when(iApiService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(),
                anyString(), anyString(), anyBoolean(), anyBoolean(), anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.pollAndPersistData( true, config);

        // Assert
        verify(iPollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
    }

    @Test
    void testPollAndPersistRDBData_exceptionAtApiLevel() throws DataPollException, APIException {
        String tableName = "testTable";
        // Arrange
        String expectedErrorMessage = "Simulated API Exception";
        when(iPollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
        var apiModel = new ApiResponseModel<Integer>();
        apiModel.setResponse(1000);
        when(iApiService.callDataCountEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString()))
                .thenReturn(apiModel);
        when(iApiService.callDataExchangeEndpoint(
                eq("testTable"),
                eq(true),
                anyString(),
                eq(false),
                anyString(),
                anyString(),
                anyBoolean(),
                anyBoolean(),
                anyString()))
                .thenThrow(new RuntimeException(expectedErrorMessage));

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.pollAndPersistData(true, config);

        // Assert
        verify(iPollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
    }



    @Test
    void testUpdateDataHelper_ExceptionAtApiLevel_StoreInSql() throws APIException {
        // Arrange
        boolean exceptionAtApiLevel = true;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        universalDataHandlingService.storeInSql = true;
        universalDataHandlingService.storeJsonInS3 = false;

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");

        var apiModel = new ApiResponseModel<String>();
        apiModel.setApiException(new APIException("Simulated API Exception"));
        // Act
        universalDataHandlingService.updateDataHelper(apiModel, timestamp,
                rawJsonData, isInitialLoad, log, timestamp, config);

        // Assert
        verify(iPollCommonService, times(1)).updateLastUpdatedTimeAndLog(eq(tableName),
                eq(timestamp), any());
        verifyNoMoreInteractions(iPollCommonService);
    }


    @Test
    void testUpdateDataHelper_ExceptionAtApiLevel_LocalDir() throws APIException {
        // Arrange
        boolean exceptionAtApiLevel = true;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        universalDataHandlingService.storeInSql = false;
        universalDataHandlingService.storeJsonInS3 = false;
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");

        var apiModel = new ApiResponseModel<String>();
        apiModel.setApiException(new APIException("TEST"));

        // Act
        universalDataHandlingService.updateDataHelper(apiModel, timestamp,
                rawJsonData, isInitialLoad, log, timestamp, config);

        // Assert
        verify(iPollCommonService, times(1)).updateLastUpdatedTimeAndLogLocalDir(
                eq(tableName), eq(timestamp), any());
        verifyNoMoreInteractions(iPollCommonService);
    }



    @Test
    void testUpdateDataHelper_NoExceptionAtApiLevel_StoreJsonInS3() throws APIException {
        // Arrange
        boolean exceptionAtApiLevel = false;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        universalDataHandlingService.storeInSql = false;
        universalDataHandlingService.storeJsonInS3 = true;

        when(is3DataService.persistToS3MultiPart(anyString(), anyString(), anyString(), any(Timestamp.class),
                anyBoolean(), any()))
                .thenReturn(new LogResponseModel(new ApiResponseModel<String>()));

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");

        var apiModel = new ApiResponseModel<String>();
        apiModel.setApiException(new APIException("Simulated API Exception"));
        // Act
        universalDataHandlingService.updateDataHelper(apiModel,
                timestamp, rawJsonData, isInitialLoad, log, timestamp, config);

        // Assert
        verify(is3DataService, times(1)).persistToS3MultiPart(
                RDB, rawJsonData, tableName, timestamp, isInitialLoad, any());
    }


    @Test
    void testUpdateDataHelper_NoExceptionAtApiLevel_LocalDir() throws APIException {
        // Arrange
        boolean exceptionAtApiLevel = false;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        universalDataHandlingService.storeInSql = false;
        universalDataHandlingService.storeJsonInS3 = false;

        when(iPollCommonService.writeJsonDataToFile(anyString(), anyString(), any(Timestamp.class),
                anyString(), any())).thenReturn(new LogResponseModel(new ApiResponseModel<String>()));
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.updateDataHelper(new ApiResponseModel<>(), timestamp,
                rawJsonData, isInitialLoad, log, timestamp, config);

        // Assert
        verify(iPollCommonService, times(1)).writeJsonDataToFile(RDB, tableName,
                timestamp, rawJsonData, any());

    }


    @Test
    void testUpdateDataHelper_ExceptionInProcessing() throws APIException {
        // Arrange
        boolean exceptionAtApiLevel = false;
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        universalDataHandlingService.storeInSql = false;
        universalDataHandlingService.storeJsonInS3 = true;

        when(is3DataService.persistToS3MultiPart(anyString(), anyString(), anyString(),
                any(Timestamp.class), anyBoolean(), any()))
                .thenThrow(new RuntimeException("S3 Error"));
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.updateDataHelper(new ApiResponseModel<>(), timestamp,
                rawJsonData, isInitialLoad, log, timestamp, config);

        // Assert
        verify(iPollCommonService, times(1))
                .updateLogNoTimestamp(eq(tableName), any());
    }

}