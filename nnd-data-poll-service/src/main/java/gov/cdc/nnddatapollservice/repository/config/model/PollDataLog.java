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

    @Column(name = "last_api_url")
    private String lastApiUrl;

    @Column(name = "last_api_header")
    private String lastApiHeader;

    @Column(name = "last_api_record_counts")
    private Integer lastApiRecordCounts;

    @Column(name = "last_total_pages")
    private Integer lastTotalPages;

    @Column(name = "last_batch_size")
    private Integer lastBatchSize;

    public PollDataLog() {}

    public PollDataLog(LogResponseModel logResponseModel, String tableName) {
        this.tableName = tableName;
        this.statusSync = logResponseModel.getStatus();
        this.startTime = logResponseModel.getStartTime();
        this.executedLog = logResponseModel.getLog();
        this.stackTrace = logResponseModel.getStackTrace();
        this.endTime = getCurrentTimestamp();
        this.lastApiUrl = logResponseModel.getApiResponseModel().getLastApiCall();
        this.lastApiHeader = logResponseModel.getApiResponseModel().getLastApiHeader();
        this.lastApiRecordCounts = logResponseModel.getApiResponseModel().getLastTotalRecordCount();
        this.lastTotalPages = logResponseModel.getApiResponseModel().getLastTotalPages();
        this.lastBatchSize = logResponseModel.getApiResponseModel().getLastBatchSize();
    }
}
