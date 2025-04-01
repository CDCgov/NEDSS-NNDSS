package gov.cdc.nnddatapollservice.universal.service;

import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.share.TimestampUtil;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("java:S125")
class RdbModernDataHandlingServiceTest {

    @Mock
    IPollCommonService iPollCommonService;
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
    void handlingExchangedData_initialLoad() throws APIException {
        universalDataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST");
        config.setLastUpdateTime(null);
        config.setTableOrder(1);
        config.setSourceDb("RDB_MODERN");
        configTableList.add(config);

        when(iPollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(iPollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(iPollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(true);

        universalDataHandlingService.handlingExchangedData("RDB");
        verify(iPollCommonService, times(1)).deleteTable(anyString());
    }

    @Test
    void handlingExchangedData_withTimestamp() throws APIException {
        universalDataHandlingService.storeInSql = true;
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("TEST");
        config.setLastUpdateTime(TimestampUtil.getCurrentTimestamp());
        config.setTableOrder(1);
        config.setSourceDb("RDB_MODERN");
        configTableList.add(config);

        when(iPollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(iPollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(iPollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(false);

        universalDataHandlingService.handlingExchangedData("RDB");
        verify(iPollCommonService, times(0)).deleteTable(anyString());
    }

    @Test
    void testRestrictFullLoad_1() throws APIException {
        // Arrange

        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder= true;
        universalDataHandlingService.edxFullSync = true;

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb(NBS_ODSE_EDX);

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);
        // Act
        universalDataHandlingService.pollAndPersistData( config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

    @Test
    void testRestrictFullLoad_2() throws APIException {
        // Arrange

        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder= true;
        universalDataHandlingService.odseFullSync = true;

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb(ODSE_OBS);

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);
        // Act
        universalDataHandlingService.pollAndPersistData( config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

    @Test
    void testRestrictFullLoad_3() throws APIException {
        // Arrange

        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder= true;
        universalDataHandlingService.odseFullSync = false;

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("TEST");

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);
        // Act
        universalDataHandlingService.pollAndPersistData( config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

    @Test
    void testRestrictFullLoad_4() throws APIException {
        // Arrange

        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder= true;
        universalDataHandlingService.edxFullSync = false;

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("TEST");

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);
        // Act
        universalDataHandlingService.pollAndPersistData( config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

    @Test
    void testRestrictFullLoad_5() throws APIException {
        // Arrange

        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder= true;
        universalDataHandlingService.edxFullSync = true;

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb(NBS_ODSE_EDX);

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);
        when(iPollCommonService.getLastUpdatedTimeLocalDir(any())).thenReturn("");
        // Act
        universalDataHandlingService.pollAndPersistData( config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

    @Test
    void testRestrictFullLoad_6() throws APIException {
        // Arrange
        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder = true;
        universalDataHandlingService.edxFullSync = true; // triggers first part of the condition

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("ODSE_OBS"); // triggers second part

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);

        // Act
        universalDataHandlingService.pollAndPersistData(config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

    @Test
    void testRestrictFullLoad_7() throws APIException {
        // Arrange
        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder = true;
        universalDataHandlingService.odseFullSync = false; // triggers first part of the condition

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb(ODSE_OBS); // triggers second part

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);

        // Act
        universalDataHandlingService.pollAndPersistData(config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

    @Test
    void testRestrictFullLoad_8() throws APIException {
        // Arrange
        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder = true;
        universalDataHandlingService.odseFullSync = true; // triggers first part of the condition

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb(ODSE_OBS + "DUMP"); // triggers second part

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);

        // Act
        universalDataHandlingService.pollAndPersistData(config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

    @Test
    void testRestrictFullLoad_9() throws APIException {
        // Arrange

        String tableName = "exampleTable";
        universalDataHandlingService.storeJsonInLocalFolder= true;
        universalDataHandlingService.edxFullSync = true;

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb(NBS_ODSE_EDX);

        when(iPollCommonService.checkInitialLoadForIndividualTable(config)).thenReturn(true);
        when(iPollCommonService.getLastUpdatedTimeLocalDir(any())).thenReturn("");
        // Act
        universalDataHandlingService.pollAndPersistData( config);

        // Assert
        verify(iPollCommonService, times(1)).checkInitialLoadForIndividualTable(any());
    }

//    private void setupServiceWithMockedDependencies() throws DataPollException {
//
//        when(iPollCommonService.decodeAndDecompress(anyString())).thenReturn("{\"data\": \"example\"}");
//        when(iPollCommonService.getCurrentTimestamp()).thenReturn("2023-01-01T00:00:00Z");
//        when(iPollCommonService.getLastUpdatedTime(anyString())).thenReturn("2023-01-01T00:00:00Z");
//
//        shareResString.setResponse("encodedData");
//        when(iApiService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(),
//                anyBoolean(), anyString(), anyString(), anyBoolean(), anyBoolean(),
//                anyString())).thenReturn(shareResString);
//
//
//
//    }

//    @Test
//    void testPollAndPersistRDBData_exception_task_failed_1() throws DataPollException, APIException {
//        String tableName = "testTable";
//        // Arrange
//        String expectedErrorMessage = "Simulated API Exception";
//        when(iPollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
//        when(iApiService.callDataCountEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString()))
//                .thenThrow(new RuntimeException(expectedErrorMessage));
//
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName(tableName);
//        config.setKeyList("key");
//        config.setSourceDb("RDB");
//        // Act
//        universalDataHandlingService.pollAndPersistData( true, config);
//        // Assert
//        verify(iPollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
//    }

//    @Test
//    void testPollAndPersistRDBData_exception_FailedTask_2() throws DataPollException, APIException {
//        String tableName = "testTable";
//        // Arrange
//        String expectedErrorMessage = "Simulated API Exception";
//        when(iPollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
//        var apiModel = new ApiResponseModel<Integer>();
//        apiModel.setResponse(1000);
//        when(iApiService.callDataCountEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString()))
//                .thenReturn(apiModel);
//        when(iApiService.callDataExchangeEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(),
//                anyString(), anyString(), anyBoolean(), anyBoolean(), anyString()))
//                .thenThrow(new RuntimeException(expectedErrorMessage));
//
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName(tableName);
//        config.setKeyList("key");
//        config.setSourceDb("RDB");
//        // Act
//        universalDataHandlingService.pollAndPersistData( true, config);
//
//        // Assert
//        verify(iPollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
//    }

//    @Test
//    void testPollAndPersistRDBData_exceptionAtApiLevel() throws DataPollException, APIException {
//        String tableName = "testTable";
//        // Arrange
//        String expectedErrorMessage = "Simulated API Exception";
//        when(iPollCommonService.getCurrentTimestamp()).thenReturn("2024-09-17T00:00:00Z");
//        var apiModel = new ApiResponseModel<Integer>();
//        apiModel.setResponse(1000);
//        when(iApiService.callDataCountEndpoint(anyString(), anyBoolean(), anyString(), anyBoolean(), anyString()))
//                .thenReturn(apiModel);
//        when(iApiService.callDataExchangeEndpoint(
//                eq("testTable"),
//                eq(true),
//                anyString(),
//                eq(false),
//                anyString(),
//                anyString(),
//                anyBoolean(),
//                anyBoolean(),
//                anyString()))
//                .thenThrow(new RuntimeException(expectedErrorMessage));
//
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName(tableName);
//        config.setKeyList("key");
//        config.setSourceDb("RDB");
//        // Act
//        universalDataHandlingService.pollAndPersistData(true, config);
//
//        // Assert
//        verify(iPollCommonService, never()).updateLastUpdatedTimeAndLog(eq(tableName), any(), any());
//    }
//


//    @Test
//    void testUpdateDataHelper_ExceptionAtApiLevel_StoreInSql() throws APIException {
//        // Arrange
//        boolean exceptionAtApiLevel = true;
//        String tableName = "NRT_OBSERVATION";
//        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
//        String rawJsonData = "{}";
//        boolean isInitialLoad = false;
//        String log = "Test Log";
//
//        universalDataHandlingService.storeInSql = true;
//        universalDataHandlingService.storeJsonInS3 = false;
//
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName(tableName);
//        config.setKeyList("key");
//        config.setSourceDb("RDB");
//
//        var apiModel = new ApiResponseModel<String>();
//        apiModel.setApiException(new APIException("Simulated API Exception"));
//        // Act
//        universalDataHandlingService.updateDataHelper(apiModel, timestamp,
//                rawJsonData, isInitialLoad, log, timestamp, config);
//
//        // Assert
//        verify(iPollCommonService, times(1)).updateLastUpdatedTimeAndLog(eq(tableName),
//                eq(timestamp), any());
//        verifyNoMoreInteractions(iPollCommonService);
//    }
//
//
//    @Test
//    void testUpdateDataHelper_ExceptionAtApiLevel_LocalDir() throws APIException {
//        // Arrange
//        boolean exceptionAtApiLevel = true;
//        String tableName = "NRT_OBSERVATION";
//        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
//        String rawJsonData = "{}";
//        boolean isInitialLoad = false;
//        String log = "Test Log";
//
//        universalDataHandlingService.storeInSql = false;
//        universalDataHandlingService.storeJsonInS3 = false;
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName(tableName);
//        config.setKeyList("key");
//        config.setSourceDb("RDB");
//
//        var apiModel = new ApiResponseModel<String>();
//        apiModel.setApiException(new APIException("TEST"));
//
//        // Act
//        universalDataHandlingService.updateDataHelper(apiModel, timestamp,
//                rawJsonData, isInitialLoad, log, timestamp, config);
//
//        // Assert
//        verify(iPollCommonService, times(1)).updateLastUpdatedTimeAndLogLocalDir(
//                eq(tableName), eq(timestamp), any());
//        verifyNoMoreInteractions(iPollCommonService);
//    }
//

    @Test
    void testUpdateDataHelper_NoExceptionAtApiLevel_StoreJsonInS3() throws APIException {
        // Arrange
        ApiResponseModel<String> apiResponseModel = new ApiResponseModel<>();
        apiResponseModel.setSuccess(true);
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        universalDataHandlingService.storeInSql = false;
        universalDataHandlingService.storeJsonInS3 = true;

        when(is3DataService.persistToS3MultiPart(anyString(), anyString(), anyString(), any(Timestamp.class), anyBoolean(), any()))
                .thenReturn(new LogResponseModel(apiResponseModel));

        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.updateDataHelper(apiResponseModel,
                timestamp, rawJsonData, isInitialLoad, log, timestamp, config);

        // Assert
        verify(is3DataService, times(1)).persistToS3MultiPart(
                eq(RDB), eq(rawJsonData), eq(tableName), eq(timestamp), eq(isInitialLoad), any());
    }


    @Test
    void testUpdateDataHelper_NoExceptionAtApiLevel_LocalDir() throws APIException {
        // Arrange
        var apiResponseModel = new ApiResponseModel<String>();
        apiResponseModel.setSuccess(true);
        String tableName = "NRT_OBSERVATION";
        Timestamp timestamp = Timestamp.valueOf("2024-10-01 12:00:00");
        String rawJsonData = "{}";
        boolean isInitialLoad = false;
        String log = "Test Log";

        universalDataHandlingService.storeInSql = false;
        universalDataHandlingService.storeJsonInS3 = false;

        when(iPollCommonService.writeJsonDataToFile(anyString(), anyString(),
                any(Timestamp.class), anyString(), any())).thenReturn(new LogResponseModel(apiResponseModel));
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.updateDataHelper(apiResponseModel, timestamp,
                rawJsonData, isInitialLoad, log, timestamp, config);

        // Assert
        verify(iPollCommonService, times(1)).writeJsonDataToFile(eq(RDB), eq(tableName),
                eq(timestamp), eq(rawJsonData), any());

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

        when(is3DataService.persistToS3MultiPart(anyString(), anyString(), anyString(), any(Timestamp.class),
                anyBoolean(), any()))
                .thenThrow(new RuntimeException("S3 Error"));
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName(tableName);
        config.setKeyList("key");
        config.setSourceDb("RDB");
        // Act
        universalDataHandlingService.updateDataHelper(new ApiResponseModel<String>(), timestamp,
                rawJsonData, isInitialLoad, log, timestamp, config);

        // Assert
        verify(iPollCommonService, times(1))
                .updateLogNoTimestamp(eq(tableName), any());
    }

}