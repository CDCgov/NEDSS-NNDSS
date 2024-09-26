package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.dto.PollDataSyncConfig;

import java.sql.Timestamp;
import java.util.List;

public interface IPollCommonService {
    Integer callDataCountEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime) throws DataPollException;
    String callDataExchangeEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime, boolean allowNull,
                             String startRow, String endRow) throws DataPollException;
    List<PollDataSyncConfig> getTableListFromConfig();
    boolean checkPollingIsInitailLoad(List<PollDataSyncConfig> configTableList);
    String getCurrentTimestamp();
    List<PollDataSyncConfig> getTablesConfigListBySOurceDB(List<PollDataSyncConfig> configTableList, String sourceDB);
    String writeJsonDataToFile(String dbSource, String tableName, Timestamp timeStamp, String jsonData);
    String getLastUpdatedTime(String tableName);
    void updateLastUpdatedTime(String tableName, Timestamp timestamp);
    String decodeAndDecompress(String base64EncodedData);
    void updateLastUpdatedTimeAndLog(String tableName, Timestamp timestamp, String log);
}
