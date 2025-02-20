package gov.cdc.nnddatapollservice.repository.config.model;

import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import static gov.cdc.nnddatapollservice.share.TimestampUtil.getCurrentTimestamp;

@Table(name = "poll_data_log")
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

    public PollDataLog() {}

    public PollDataLog(LogResponseModel logResponseModel, String tableName) {
        this.tableName = tableName;
        this.statusSync = logResponseModel.getStatus();
        this.startTime = logResponseModel.getStartTime();
        this.executedLog = logResponseModel.getLog();
        this.stackTrace = logResponseModel.getStackTrace();
        this.endTime = getCurrentTimestamp();
    }
}
