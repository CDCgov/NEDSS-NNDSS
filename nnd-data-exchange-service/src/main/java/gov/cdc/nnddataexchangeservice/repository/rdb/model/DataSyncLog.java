package gov.cdc.nnddataexchangeservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "data_sync_log")
@Getter
@Setter
public class DataSyncLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", updatable = false, nullable = false)
    private Long logId;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "status_sync", nullable = false, length = 20)
    private String statusSync;

    @Column(name = "error_desc", columnDefinition = "NVARCHAR(MAX)")
    private String errorDesc;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;



    @Column(name = "log_start_row")
    private String startRow;

    @Column(name = "log_end_row")
    private String endRow;

    @Column(name = "last_executed_run_time")
    private String lastExecutedRunTime;

    @Column(name = "last_executed_result_count")
    private Integer lastExecutedResultCount;

    @Column(name = "last_executed_timestamp")
    private Timestamp lastExecutedTimestamp;
}
