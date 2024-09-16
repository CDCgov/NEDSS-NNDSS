package gov.cdc.nnddatapollservice.rdbmodern.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.rdbmodern.dao.RdbModernDataPersistentDAO;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
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
    @InjectMocks
    private RdbModernDataHandlingService rdbModernDataHandlingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handlingExchangedData_initialLoad() throws DataPollException {
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
}