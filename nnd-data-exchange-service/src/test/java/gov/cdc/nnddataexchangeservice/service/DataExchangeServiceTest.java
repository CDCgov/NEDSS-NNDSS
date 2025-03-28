package gov.cdc.nnddataexchangeservice.service;


import com.google.gson.Gson;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.rdb.DataSyncConfigRepository;
import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataSyncConfig;
import gov.cdc.nnddataexchangeservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddataexchangeservice.service.interfaces.INetsstTransportService;
import gov.cdc.nnddataexchangeservice.service.interfaces.ITransportQOutService;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;
import gov.cdc.nnddataexchangeservice.service.model.dto.CNTransportQOutDto;
import gov.cdc.nnddataexchangeservice.service.model.dto.NETSSTransportQOutDto;
import gov.cdc.nnddataexchangeservice.service.model.dto.TransportQOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataExchangeServiceTest {

    @Mock
    private INetsstTransportService netsstTransportService;

    @Mock
    private ITransportQOutService transportQOutService;

    @Mock
    private ICNTransportQOutService cnTransportQOutService;

    @Mock
    private Gson gson;

    @InjectMocks
    private DataExchangeService dataExchangeService;

    @Mock
    private DataSyncConfigRepository dataSyncConfigRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DataExchangeGenericService dataExchangeGenericService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDataForOnPremExchanging() throws Exception {
        String cnStatusTime = "2023-07-30 10:00:00.000";
        String transportTime = "2023-07-30 10:00:00.000";
        String netssTime = "2023-07-30 10:00:00.000";
        String statusCd = "status";

        List<CNTransportQOutDto> cnTransportQOutDtos = Collections.singletonList(new CNTransportQOutDto());
        List<TransportQOutDto> transportQOutDtos = Collections.singletonList(new TransportQOutDto());
        List<NETSSTransportQOutDto> netssTransportQOutDtos = Collections.singletonList(new NETSSTransportQOutDto());
        when(gson.toJson(any(DataExchangeModel.class))).thenReturn("Test");
        when(cnTransportQOutService.getTransportData(statusCd, cnStatusTime, 0)).thenReturn(cnTransportQOutDtos);
        when(transportQOutService.getTransportData(transportTime, 0)).thenReturn(transportQOutDtos);
        when(netsstTransportService.getNetssTransportData(netssTime, 0)).thenReturn(netssTransportQOutDtos);


        String result = dataExchangeService.getDataForOnPremExchanging(cnStatusTime, transportTime, netssTime, statusCd, 0, false);

        assertNotNull(result);

        verify(cnTransportQOutService, times(1)).getTransportData(statusCd, cnStatusTime, 0);
        verify(transportQOutService, times(1)).getTransportData(transportTime, 0);
        verify(netsstTransportService, times(1)).getNetssTransportData(netssTime, 0);
    }

    @Test
    void testGetDataForOnPremExchanging_True() throws Exception {
        String cnStatusTime = "2023-07-30 10:00:00.000";
        String transportTime = "2023-07-30 10:00:00.000";
        String netssTime = "2023-07-30 10:00:00.000";
        String statusCd = "status";

        List<CNTransportQOutDto> cnTransportQOutDtos = Collections.singletonList(new CNTransportQOutDto());
        List<TransportQOutDto> transportQOutDtos = Collections.singletonList(new TransportQOutDto());
        List<NETSSTransportQOutDto> netssTransportQOutDtos = Collections.singletonList(new NETSSTransportQOutDto());



        when(cnTransportQOutService.getTransportData(statusCd, cnStatusTime, 0)).thenReturn(cnTransportQOutDtos);
        when(transportQOutService.getTransportData(transportTime, 0)).thenReturn(transportQOutDtos);
        when(netsstTransportService.getNetssTransportData(netssTime, 0)).thenReturn(netssTransportQOutDtos);
        when(gson.toJson(any(DataExchangeModel.class))).thenReturn("Test");

        String result = dataExchangeService.getDataForOnPremExchanging(cnStatusTime, transportTime, netssTime, statusCd, 0, true);

        assertNotNull(result);

        verify(cnTransportQOutService, times(1)).getTransportData(statusCd, cnStatusTime, 0);
        verify(transportQOutService, times(1)).getTransportData(transportTime, 0);
        verify(netsstTransportService, times(1)).getNetssTransportData(netssTime, 0);
    }

    @Test
    void testGetDataForOnPremExchanging_Exception() throws Exception {
        String cnStatusTime = "2023-07-30 10:00:00.000";
        String transportTime = "2023-07-30 10:00:00.000";
        String netssTime = "2023-07-30 10:00:00.000";
        String statusCd = "status";

        when(cnTransportQOutService.getTransportData(statusCd, cnStatusTime, 0)).thenThrow(new DataExchangeException("Exception"));

        assertThrows(DataExchangeException.class, () -> dataExchangeService.getDataForOnPremExchanging(cnStatusTime, transportTime, netssTime, statusCd, 0, false));

        verify(cnTransportQOutService, times(1)).getTransportData(statusCd, cnStatusTime, 0);
        verify(transportQOutService, times(0)).getTransportData(transportTime,0);
        verify(netsstTransportService, times(0)).getNetssTransportData(netssTime,0);
    }

    @Test
    void testGetAllTablesCount() throws DataExchangeException {
        String sourceDbName = "RDB";
        String tableName = null;
        String timestamp = "2023-01-01 00:00:00.000";
        boolean initialLoad = true;

        DataSyncConfig dataSyncConfig = new DataSyncConfig();
        dataSyncConfig.setTableName("D_INV_ADMINISTRATIVE");
        dataSyncConfig.setSourceDb("RDB");
        dataSyncConfig.setQueryCount("SELECT COUNT(*) FROM D_INV_ADMINISTRATIVE WHERE timestamp > ?");

        List<DataSyncConfig> dataSyncConfigList = Collections.singletonList(dataSyncConfig);

        when(dataSyncConfigRepository.findBySourceDb(sourceDbName)).thenReturn(dataSyncConfigList);
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(10);

        List<Map<String, Object>> result = dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, initialLoad);

        assertNotNull(result);
        assertEquals(1, result.size());
        Map<String, Object> resultMap = result.get(0);
        assertEquals("D_INV_ADMINISTRATIVE", resultMap.get("Table Name"));
        assertEquals("RDB", resultMap.get("Source Database Name"));
        assertEquals(10, resultMap.get("Record Count"));
    }

    @Test
    void testGetAllTablesCount_EmptyList() throws DataExchangeException {
        String sourceDbName = "RDB";
        String tableName = null;
        String timestamp = "2023-01-01 00:00:00.000";
        boolean initialLoad = true;

        when(dataSyncConfigRepository.findBySourceDb(sourceDbName)).thenReturn(Collections.emptyList());

        List<Map<String, Object>> result = dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, initialLoad);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllTablesCount_NullTimestamp() throws DataExchangeException {
        String sourceDbName = "RDB";
        String tableName = null;
        String timestamp = null; // Null timestamp
        boolean initialLoad = true;

        DataSyncConfig dataSyncConfig = new DataSyncConfig();
        dataSyncConfig.setTableName("D_INV_ADMINISTRATIVE");
        dataSyncConfig.setSourceDb("RDB");
        dataSyncConfig.setQueryCount("SELECT COUNT(*) FROM D_INV_ADMINISTRATIVE WHERE timestamp > ?");

        List<DataSyncConfig> dataSyncConfigList = Collections.singletonList(dataSyncConfig);

        when(dataSyncConfigRepository.findBySourceDb(sourceDbName)).thenReturn(dataSyncConfigList);
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(10);

        List<Map<String, Object>> result = dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, initialLoad);

        assertNotNull(result);
        assertEquals(1, result.size());
        Map<String, Object> resultMap = result.get(0);
        assertEquals("D_INV_ADMINISTRATIVE", resultMap.get("Table Name"));
        assertEquals("RDB", resultMap.get("Source Database Name"));
        assertEquals(10, resultMap.get("Record Count"));
    }

    @Test
    void testGetAllTablesCount_NoRecordsReturned() throws DataExchangeException {
        String sourceDbName = "RDB";
        String tableName = null;
        String timestamp = "2023-01-01 00:00:00.000";
        boolean initialLoad = true;

        DataSyncConfig dataSyncConfig = new DataSyncConfig();
        dataSyncConfig.setTableName("D_INV_ADMINISTRATIVE");
        dataSyncConfig.setSourceDb("RDB");
        dataSyncConfig.setQueryCount("SELECT COUNT(*) FROM D_INV_ADMINISTRATIVE WHERE timestamp > ?");

        List<DataSyncConfig> dataSyncConfigList = Collections.singletonList(dataSyncConfig);

        when(dataSyncConfigRepository.findBySourceDb(sourceDbName)).thenReturn(dataSyncConfigList);
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0); // No records

        List<Map<String, Object>> result = dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, initialLoad);

        assertNotNull(result);
        assertEquals(1, result.size());
        Map<String, Object> resultMap = result.get(0);
        assertEquals("D_INV_ADMINISTRATIVE", resultMap.get("Table Name"));
        assertEquals("RDB", resultMap.get("Source Database Name"));
        assertEquals(0, resultMap.get("Record Count"));
    }

    @Test
    void testGetAllTablesCount_MultipleTables() throws DataExchangeException {
        String sourceDbName = "RDB";
        String tableName = null;
        String timestamp = "2023-01-01 00:00:00.000";
        boolean initialLoad = true;

        DataSyncConfig dataSyncConfig1 = new DataSyncConfig();
        dataSyncConfig1.setTableName("D_INV_ADMINISTRATIVE");
        dataSyncConfig1.setSourceDb("RDB");
        dataSyncConfig1.setQueryCount("SELECT COUNT(*) FROM D_INV_ADMINISTRATIVE WHERE timestamp > ?");

        DataSyncConfig dataSyncConfig2 = new DataSyncConfig();
        dataSyncConfig2.setTableName("D_INV_EPIDEMIOLOGY");
        dataSyncConfig2.setSourceDb("RDB");
        dataSyncConfig2.setQueryCount("SELECT COUNT(*) FROM D_INV_EPIDEMIOLOGY WHERE timestamp > ?");

        List<DataSyncConfig> dataSyncConfigList = Arrays.asList(dataSyncConfig1, dataSyncConfig2);

        when(dataSyncConfigRepository.findBySourceDb(sourceDbName)).thenReturn(dataSyncConfigList);
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(10).thenReturn(15); // Different counts for each table

        List<Map<String, Object>> result = dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, initialLoad);

        assertNotNull(result);
        assertEquals(2, result.size());

        Map<String, Object> resultMap1 = result.get(0);
        assertEquals("D_INV_ADMINISTRATIVE", resultMap1.get("Table Name"));
        assertEquals("RDB", resultMap1.get("Source Database Name"));
        assertEquals(10, resultMap1.get("Record Count"));

        Map<String, Object> resultMap2 = result.get(1);
        assertEquals("D_INV_EPIDEMIOLOGY", resultMap2.get("Table Name"));
        assertEquals("RDB", resultMap2.get("Source Database Name"));
        assertEquals(15, resultMap2.get("Record Count"));
    }
}