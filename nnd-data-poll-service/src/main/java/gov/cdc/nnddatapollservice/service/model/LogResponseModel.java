package gov.cdc.nnddatapollservice.service.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LogResponseModel {
    public String log;
    public String stackTrace;
    public String status;
    public Timestamp startTime;

    public LogResponseModel() {

    }

    public LogResponseModel(String log, String stackTrace, String status, Timestamp startTime) {
        this.log = log;
        this.stackTrace = stackTrace;
        this.status = status;
        this.startTime = startTime;
    }

    public LogResponseModel(String status) {
        this.status = status;
    }
}
