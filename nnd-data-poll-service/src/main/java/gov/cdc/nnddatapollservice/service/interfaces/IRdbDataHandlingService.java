package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;

import java.util.List;

public interface IRdbDataHandlingService {
    void handlingExchangedData(String tableName) throws DataPollException;
    void persistRdbData(String tableName, String jsonData) throws DataPollException;
    List<PollDataSyncConfig> getTableListFromConfig() throws DataPollException;
}
