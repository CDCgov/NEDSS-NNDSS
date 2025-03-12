package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;

import java.sql.Timestamp;
import java.util.List;

public interface IPollCommonService {
    List<PollDataSyncConfig> getTableListFromConfig();
    boolean checkPollingIsInitailLoad(List<PollDataSyncConfig> configTableList);
    String getCurrentTimestamp();
    List<PollDataSyncConfig> getTablesConfigListBySOurceDB(List<PollDataSyncConfig> configTableList, String sourceDB);
    LogResponseModel writeJsonDataToFile(String dbSource, String tableName, Timestamp timeStamp, String jsonData, ApiResponseModel<?> apiResponseModel);
    String getLastUpdatedTime(String tableName);
    void updateLastUpdatedTime(String tableName, Timestamp timestamp);
    String decodeAndDecompress(String base64EncodedData);

    String getLastUpdatedTimeS3(String tableName);

    String getLastUpdatedTimeLocalDir(String tableName);

    void updateLastUpdatedTimeAndLog(String tableName, Timestamp timestamp, LogResponseModel logResponseModel);

    void updateLastUpdatedTimeAndLogS3(String tableName, Timestamp timestamp, LogResponseModel logResponseModel);

    void updateLastUpdatedTimeAndLogLocalDir(String tableName, Timestamp timestamp, LogResponseModel logResponseModel);

    void deleteTable(String tableName);

    void updateLogNoTimestamp(String tableName, LogResponseModel logResponseModel);

    String getMaxId(String tableName, String key);
}
