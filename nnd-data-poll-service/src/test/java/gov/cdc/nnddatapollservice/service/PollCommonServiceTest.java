//package gov.cdc.nnddatapollservice.service;
//
//import gov.cdc.nnddatapollservice.exception.DataPollException;
//import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
//import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
//import gov.cdc.nnddatapollservice.share.JdbcTemplateUtil;
//import gov.cdc.nnddatapollservice.share.PollServiceUtil;
//import gov.cdc.nnddatapollservice.share.TimestampUtil;
//import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.mockito.stubbing.Answer;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class PollCommonServiceTest {
//
//    @Mock
//    private JdbcTemplateUtil jdbcTemplateUtil;
//    @Mock
//    private ITokenService tokenService;
//
//    @Mock
//    private RestTemplate restTemplate;
//    @InjectMocks
//    private PollCommonService iPollCommonService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCallDataCountEndpoint_Success() {
//        // Arrange
//        iPollCommonService.exchangeTotalRecordEndpoint = "http://ip.jsontest.com/";
//        String tableName = "NRT_OBSERVATION";
//        boolean isInitialLoad = true;
//        String lastUpdatedTime = "2024-10-01";
//        String token = "sampleToken";
//
//        when(tokenService.getToken()).thenReturn(token);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.add("clientid", "clientId");
//        headers.add("clientsecret", "clientSecret");
//        headers.add("initialLoad", String.valueOf(isInitialLoad));
//
//        when(tokenService.getToken()).thenReturn("testtoken");
//
//        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
//        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(mockResponse);
//
//        // Act
//        assertThrows(DataPollException.class,
//                () ->
//                        iPollCommonService.callDataCountEndpoint(tableName, isInitialLoad, lastUpdatedTime));
//
//    }
//
//
//    @Test
//    void testPersistingExchangeData_Exception() {
//        String timestamp = "2024-09-15 10:15:20.123";
//        assertThrows(DataPollException.class, () -> iPollCommonService
//                .callDataExchangeEndpoint("TEST_TABLE", true, timestamp,
//                        true, "0", "1", false));
//
//        verify(tokenService, times(1)).getToken();
//    }
//
//    @Test
//    void checkPollingIsInitailLoad_true() {
//        List<PollDataSyncConfig> configTableList = new ArrayList<>();
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName("D_ORGANIZATION");
//        config.setLastUpdateTime(null);
//        config.setTableOrder(1);
//        config.setQuery("");
//        configTableList.add(config);
//
//        boolean isInitailLoad = iPollCommonService.checkPollingIsInitailLoad(configTableList);
//        assertTrue(isInitailLoad);
//    }
//
//    @Test
//    void checkPollingIsInitailLoad_false() {
//        List<PollDataSyncConfig> configTableList = new ArrayList<>();
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName("D_ORGANIZATION");
//        config.setLastUpdateTime(TimestampUtil.getCurrentTimestamp());
//        config.setTableOrder(1);
//        config.setQuery("");
//        configTableList.add(config);
//
//        boolean isInitailLoad = iPollCommonService.checkPollingIsInitailLoad(configTableList);
//        assertFalse(isInitailLoad);
//    }
//
//    @Test
//    void getCurrentTimestamp() {
//        String timestamp = iPollCommonService.getCurrentTimestamp();
//        assertNotNull(timestamp);
//    }
//
//    @Test
//    void getTableListFromConfig() {
//        List<PollDataSyncConfig> configTableList = new ArrayList<>();
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName("D_ORGANIZATION");
//        config.setLastUpdateTime(TimestampUtil.getCurrentTimestamp());
//        config.setTableOrder(1);
//        config.setQuery("");
//        configTableList.add(config);
//
//        when(iPollCommonService.getTableListFromConfig()).thenReturn(configTableList);
//
//        List<PollDataSyncConfig> tablesListActual = iPollCommonService.getTableListFromConfig();
//        assertEquals(1, tablesListActual.size());
//    }
//
//    @Test
//    void getLastUpdatedTime() {
//        String timestamp = "2024-09-15 10:15:20.123";
//        when(iPollCommonService.getLastUpdatedTime(anyString())).thenReturn(timestamp);
//        String lastupdatedTime = iPollCommonService.getLastUpdatedTime("TEST_TABLE");
//        assertEquals(timestamp, lastupdatedTime);
//    }
//
//    @Test
//    void updateLastUpdatedTime() {
//        iPollCommonService.updateLastUpdatedTime("TEST_TABLE", TimestampUtil.getCurrentTimestamp());
//        verify(jdbcTemplateUtil).updateLastUpdatedTime(anyString(), any(Timestamp.class));
//    }
//
//    @Test
//    void getTablesConfigListBySOurceDB() {
//        List<PollDataSyncConfig> configTableList = new ArrayList<>();
//        PollDataSyncConfig config = new PollDataSyncConfig();
//        config.setTableName("D_ORGANIZATION");
//        config.setLastUpdateTime(TimestampUtil.getCurrentTimestamp());
//        config.setTableOrder(1);
//        config.setQuery("");
//        config.setSourceDb("RDB");
//        configTableList.add(config);
//
//        List<PollDataSyncConfig> configTableListActual = iPollCommonService.getTablesConfigListBySOurceDB(configTableList, "RDB");
//        assertEquals(1, configTableListActual.size());
//    }
//
//    @Test
//    void decodeAndDecompress() {
//        String rawdata = iPollCommonService.decodeAndDecompress("testdata123");
//        assertEquals("testdata123", rawdata);
//    }
//
//    @Test
//    void writeJsonDataToFile() {
//        ReflectionTestUtils.setField(iPollCommonService, "datasyncLocalFilePath", System.getProperty("java.io.tmpdir"));
//        try (MockedStatic<PollServiceUtil> mocked = Mockito.mockStatic(PollServiceUtil.class)) {
//            mocked.when(() -> PollServiceUtil.writeJsonToFile(any(), any(), anyString(), any(), anyString()))
//                    .thenAnswer((Answer<Void>) invocation -> null);
//            iPollCommonService.writeJsonDataToFile("RDB", "TEST_TABLE",
//                    TimestampUtil.getCurrentTimestamp(), "TEST DATA");
//            mocked.verify(() -> PollServiceUtil.writeJsonToFile(any(), any(), any(), any(), any()));
//        }
//    }
//
//    @Test
//    void testGetLastUpdatedTimeS3() {
//        // Arrange
//        String tableName = "testTable";
//        String expectedTime = "2024-09-30 12:00:00";
//        when(jdbcTemplateUtil.getLastUpdatedTimeS3(tableName)).thenReturn(expectedTime);
//
//        // Act
//        String result = iPollCommonService.getLastUpdatedTimeS3(tableName);
//
//        // Assert
//        assertEquals(expectedTime, result);
//        verify(jdbcTemplateUtil, times(1)).getLastUpdatedTimeS3(tableName);
//    }
//
//    @Test
//    void testGetLastUpdatedTimeLocalDir() {
//        // Arrange
//        String tableName = "testTable";
//        String expectedTime = "2024-09-30 12:00:00";
//        when(jdbcTemplateUtil.getLastUpdatedTimeLocalDir(tableName)).thenReturn(expectedTime);
//
//        // Act
//        String result = iPollCommonService.getLastUpdatedTimeLocalDir(tableName);
//
//        // Assert
//        assertEquals(expectedTime, result);
//        verify(jdbcTemplateUtil, times(1)).getLastUpdatedTimeLocalDir(tableName);
//    }
//
//    @Test
//    void testUpdateLastUpdatedTimeAndLog() {
//        // Arrange
//        String tableName = "testTable";
//        Timestamp timestamp = Timestamp.valueOf("2024-09-30 12:00:00");
//        String log = "Update successful";
//
//        // Act
//        iPollCommonService.updateLastUpdatedTimeAndLog(tableName, timestamp, new LogResponseModel());
//
//        // Assert
//        verify(jdbcTemplateUtil, times(1)).updateLastUpdatedTimeAndLog(eq(tableName), eq(timestamp), any());
//    }
//
//    @Test
//    void testUpdateLastUpdatedTimeAndLogS3() {
//        // Arrange
//        String tableName = "testTable";
//        Timestamp timestamp = Timestamp.valueOf("2024-09-30 12:00:00");
//        String log = "S3 Update successful";
//
//        // Act
//        iPollCommonService.updateLastUpdatedTimeAndLogS3(tableName, timestamp, new LogResponseModel());
//
//        // Assert
//        verify(jdbcTemplateUtil, times(1)).updateLastUpdatedTimeAndLogS3(eq(tableName), eq(timestamp), any());
//    }
//
//    @Test
//    void testUpdateLastUpdatedTimeAndLogLocalDir() {
//        // Arrange
//        String tableName = "testTable";
//        Timestamp timestamp = Timestamp.valueOf("2024-09-30 12:00:00");
//        String log = "Local Dir Update successful";
//
//        // Act
//        iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(tableName, timestamp, new LogResponseModel());
//
//        // Assert
//        verify(jdbcTemplateUtil, times(1)).updateLastUpdatedTimeAndLogLocalDir(eq(tableName),
//                eq(timestamp), any());
//    }
//
//
//}