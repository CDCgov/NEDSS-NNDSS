package gov.cdc.nnddataexchangeservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "data_sync_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSyncConfig {
    @Id
    @Column(name = "table_name")
    private String tableName;

    @Column(name = "source_db")
    private String sourceDb;

    @Column(name = "query")
    private String query;


    @Column(name = "query_with_null_timestamp")
    private String queryWithNullTimeStamp;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "last_executed_run_time")
    private String lastExecutedRunTime;

    @Column(name = "last_executed_result_count")
    private Integer lastExecutedResultCount;

    @Column(name = "last_executed_timestamp")
    private Timestamp lastExecutedTimestamp;

    @Column(name = "query_count")
    private String queryCount;

    @Column(name = "query_with_pagination")
    private String queryWithPagination;

    @Column(name = "log_start_row")
    private String startRow;

    @Column(name = "log_end_row")
    private String endRow;

}
