package gov.cdc.nnddatapollservice.srte.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.srte.dao.SrteDataPersistentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SrteDataHandlingServiceTest {

    @Mock
    private SrteDataPersistentDAO srteDataPersistentDAO;
    @Mock
    IPollCommonService pollCommonService;
    @InjectMocks
    private SrteDataHandlingService srteDataHandlingService;

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
        config.setSourceDb("SRTE");
        configTableList.add(config);

        when(pollCommonService.getTableListFromConfig()).thenReturn(configTableList);
        when(pollCommonService.getTablesConfigListBySOurceDB(anyList(), anyString())).thenReturn(configTableList);
        when(pollCommonService.checkPollingIsInitailLoad(configTableList)).thenReturn(true);

        srteDataHandlingService.handlingExchangedData();
        verify(srteDataPersistentDAO, times(1)).deleteTable(anyString());
        verify(srteDataPersistentDAO, times(1)).saveSRTEData(any(), any());
    }
}