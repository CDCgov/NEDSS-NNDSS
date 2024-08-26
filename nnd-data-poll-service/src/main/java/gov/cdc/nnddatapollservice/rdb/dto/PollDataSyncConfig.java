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
    private String tableOrder;
    private String query;
}
