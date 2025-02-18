package gov.cdc.nnddatapollservice.repository.config.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class PollDataLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @Column(name = "table_name", nullable = false, length = 255)
    private String tableName;

    @Column(name = "status_sync", nullable = false, length = 20)
    private String statusSync;

    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "executed_log")
    private String executedLog;

    @Column(name = "stack_trace")
    private String stackTrace;

}
