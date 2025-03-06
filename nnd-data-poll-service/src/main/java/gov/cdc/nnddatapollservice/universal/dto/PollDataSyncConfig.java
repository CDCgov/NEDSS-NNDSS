package gov.cdc.nnddatapollservice.universal.dto;

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
    private String keyList = "";
    private boolean recreateApplied;
    private boolean noPagination;
}
