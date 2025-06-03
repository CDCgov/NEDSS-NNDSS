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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
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

    @ParameterizedTest
    @NullSource
    void testGetAllTablesCount_NullTimestamp(String timestamp) throws DataExchangeException {
        String sourceDbName = "RDB";

        DataSyncConfig config = new DataSyncConfig();
        config.setTableName("D_INV_ADMINISTRATIVE");
        config.setSourceDb("RDB");
        config.setQueryCount("SELECT COUNT(*) FROM D_INV_ADMINISTRATIVE WHERE timestamp > ?");
        config.setPartOfDatasync(true);
        when(dataSyncConfigRepository.findBySourceDb(sourceDbName)).thenReturn(Collections.singletonList(config));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(10);

        List<Map<String, Object>> result = dataExchangeGenericService.getAllTablesCount(sourceDbName, null, timestamp, false);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10, result.getFirst().get("Record Count"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "2023-01-01 00:00:00.000"})
    void testGetAllTablesCount_DifferentTimestamps(String timestamp) throws DataExchangeException {
        String sourceDbName = "RDB";

        DataSyncConfig config = new DataSyncConfig();
        config.setTableName("D_INV_ADMINISTRATIVE");
        config.setSourceDb("RDB");
        config.setQueryCount("SELECT COUNT(*) FROM D_INV_ADMINISTRATIVE WHERE timestamp > ?");
        config.setPartOfDatasync(true);

        when(dataSyncConfigRepository.findBySourceDb(sourceDbName)).thenReturn(Collections.singletonList(config));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(5);

        List<Map<String, Object>> result = dataExchangeGenericService.getAllTablesCount(sourceDbName, null, timestamp, false);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.getFirst().get("Record Count"));
    }

    @ParameterizedTest
    @CsvSource({"Table1", "Table2", "Table3"})
    void testGetAllTablesCount_DifferentTables(String tableName) throws DataExchangeException {
        String sourceDbName = "RDB";
        String timestamp = "2023-01-01 00:00:00.000";

        DataSyncConfig config = new DataSyncConfig();
        config.setTableName(tableName);
        config.setSourceDb("RDB");
        config.setQueryCount("SELECT COUNT(*) FROM " + tableName + " WHERE timestamp > ?");
        config.setPartOfDatasync(true);
        when(dataSyncConfigRepository.findByTableNameAndSourceDb(tableName, sourceDbName)).thenReturn(Collections.singletonList(config));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(7);

        List<Map<String, Object>> result = dataExchangeGenericService.getAllTablesCount(sourceDbName, tableName, timestamp, false);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(7, result.getFirst().get("Record Count"));
    }

    @ParameterizedTest
    @CsvSource({
            "'', '', 0",
            "'table1', '', 1",
            "'table1,table2', '', 2",
            "'', 'sourceDb1', 3",
            "'table1', 'sourceDb1', 4"
    })
    void testFetchDataSyncConfigs(String tableName, String sourceDbName, int expectedMethodCall) {
        DataExchangeGenericService spyService = spy(dataExchangeGenericService); // NOSONAR
        List<DataSyncConfig> result = spyService.fetchDataSyncConfigs(sourceDbName, tableName); // NOSONAR

        switch (expectedMethodCall) {
            case 0:
                verify(dataSyncConfigRepository, times(1)).findAll();
                break;
            case 1:
                verify(dataSyncConfigRepository, times(1)).findByTableName(tableName);
                break;
            case 2:
                verify(dataSyncConfigRepository, times(1)).findByTableNameIn(Arrays.asList(tableName.split("\\s*,\\s*")));
                break;
            case 3:
                verify(dataSyncConfigRepository, times(1)).findBySourceDb(sourceDbName);
                break;
            case 4:
                verify(dataSyncConfigRepository, times(1)).findByTableNameAndSourceDb(tableName, sourceDbName);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + expectedMethodCall);
        }
    }
}