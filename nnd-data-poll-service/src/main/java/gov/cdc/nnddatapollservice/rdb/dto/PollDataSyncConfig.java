package gov.cdc.nnddatapollservice.rdb.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PollDataSyncConfig {
    private String tableName;
    private String sourceDb;
    private Timestamp lastUpdateTime;
    private Timestamp lastUpdateTimeS3;
    private Timestamp lastUpdateTimeLocalDir;
    private String lastExecutedLog;
    private int tableOrder;
    private String query;
    private String keyList;
    private boolean recreateApplied;
}
