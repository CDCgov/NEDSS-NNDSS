package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dao.RdbDataPersistentDAO;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.ITokenService;
import gov.cdc.nnddatapollservice.share.PollServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PollCommonServiceTest {

    @Mock
    private ITokenService tokenService;
    @Mock
    private RdbDataPersistentDAO rdbDataPersistentDAO;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private PollCommonService pollCommonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCallDataCountEndpoint_Success() {
        // Arrange
        pollCommonService.exchangeTotalRecordEndpoint = "http://ip.jsontest.com/";
        String tableName = "NRT_OBSERVATION";
        boolean isInitialLoad = true;
        String lastUpdatedTime = "2024-10-01";
        String token = "sampleToken";

        when(tokenService.getToken()).thenReturn(token);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.add("clientid", "clientId");
        headers.add("clientsecret", "clientSecret");
        headers.add("initialLoad", String.valueOf(isInitialLoad));

        when(tokenService.getToken()).thenReturn("testtoken");

        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        // Act
        assertThrows(DataPollException.class,
                () ->
                        pollCommonService.callDataCountEndpoint(tableName, isInitialLoad, lastUpdatedTime));

    }


    @Test
    void handlingExchangedData_initialLoad() throws DataPollException {
        pollCommonService.exchangeEndpoint = "http://ip.jsontest.com/";
        String timestamp = "2024-09-15 10:15:20.123";
        when(tokenService.getToken()).thenReturn("testtoken");

        ResponseEntity<String> mockResponse = ResponseEntity.ok("Mock Response Body");
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        pollCommonService.callDataExchangeEndpoint("TEST_TABLE", true, timestamp, true, "0", "1");
        verify(tokenService, times(1)).getToken();
    }

    @Test
    void testPersistingExchangeData_Exception() {
        String timestamp = "2024-09-15 10:15:20.123";
        assertThrows(DataPollException.class, () -> pollCommonService.callDataExchangeEndpoint("TEST_TABLE", true, timestamp, true, "0", "1"));

        verify(tokenService, times(1)).getToken();
    }

    @Test
    void checkPollingIsInitailLoad_true() {
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_ORGANIZATION");
        config.setLastUpdateTime(null);
        config.setTableOrder(1);
        config.setQuery("");
        configTableList.add(config);

        boolean isInitailLoad = pollCommonService.checkPollingIsInitailLoad(configTableList);
        assertTrue(isInitailLoad);
    }

    @Test
    void checkPollingIsInitailLoad_false() {
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_ORGANIZATION");
        config.setLastUpdateTime(Timestamp.from(Instant.now()));
        config.setTableOrder(1);
        config.setQuery("");
        configTableList.add(config);

        boolean isInitailLoad = pollCommonService.checkPollingIsInitailLoad(configTableList);
        assertFalse(isInitailLoad);
    }

    @Test
    void getCurrentTimestamp() {
        String timestamp = pollCommonService.getCurrentTimestamp();
        assertNotNull(timestamp);
    }

    @Test
    void getTableListFromConfig() {
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_ORGANIZATION");
        config.setLastUpdateTime(Timestamp.from(Instant.now()));
        config.setTableOrder(1);
        config.setQuery("");
        configTableList.add(config);

        when(pollCommonService.getTableListFromConfig()).thenReturn(configTableList);

        List<PollDataSyncConfig> tablesListActual = pollCommonService.getTableListFromConfig();
        assertEquals(1, tablesListActual.size());
    }

    @Test
    void getLastUpdatedTime() {
        String timestamp = "2024-09-15 10:15:20.123";
        when(rdbDataPersistentDAO.getLastUpdatedTime(anyString())).thenReturn(timestamp);
        String lastupdatedTime = pollCommonService.getLastUpdatedTime("TEST_TABLE");
        assertEquals(timestamp, lastupdatedTime);
    }

    @Test
    void updateLastUpdatedTime() {
        pollCommonService.updateLastUpdatedTime("TEST_TABLE", Timestamp.from(Instant.now()));
        verify(rdbDataPersistentDAO).updateLastUpdatedTime(anyString(), any(Timestamp.class));
    }

    @Test
    void getTablesConfigListBySOurceDB() {
        List<PollDataSyncConfig> configTableList = new ArrayList<>();
        PollDataSyncConfig config = new PollDataSyncConfig();
        config.setTableName("D_ORGANIZATION");
        config.setLastUpdateTime(Timestamp.from(Instant.now()));
        config.setTableOrder(1);
        config.setQuery("");
        config.setSourceDb("RDB");
        configTableList.add(config);

        List<PollDataSyncConfig> configTableListActual = pollCommonService.getTablesConfigListBySOurceDB(configTableList, "RDB");
        assertEquals(1, configTableListActual.size());
    }

    @Test
    void decodeAndDecompress() {
        String rawdata = pollCommonService.decodeAndDecompress("testdata123");
        assertEquals("testdata123", rawdata);
    }

    @Test
    void writeJsonDataToFile() {
        ReflectionTestUtils.setField(pollCommonService, "datasyncLocalFilePath", System.getProperty("java.io.tmpdir"));
        try (MockedStatic<PollServiceUtil> mocked = Mockito.mockStatic(PollServiceUtil.class)) {
            mocked.when(() -> PollServiceUtil.writeJsonToFile(any(), any(), anyString(), any(), anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);
            pollCommonService.writeJsonDataToFile("RDB", "TEST_TABLE", Timestamp.from(Instant.now()), "TEST DATA");
            mocked.verify(() -> PollServiceUtil.writeJsonToFile(any(), any(), any(), any(), any()));
        }
    }
}