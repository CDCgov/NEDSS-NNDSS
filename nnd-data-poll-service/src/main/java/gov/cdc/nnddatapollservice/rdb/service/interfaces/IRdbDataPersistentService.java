package gov.cdc.nnddatapollservice.rdb.service.interfaces;

import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;

import java.util.List;

public interface IRdbDataPersistentService {
    void saveRDBData(String tableName, String jsonData);
    String getLastUpdatedTime(String tableName);
    void updateLastUpdatedTime(String tableName);
    List<PollDataSyncConfig> getTableListFromConfig();
}