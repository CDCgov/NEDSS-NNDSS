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

    public ApiResponseModel<?> apiResponseModel;
    public LogResponseModel(ApiResponseModel<?> apiResponseModel) {
        this.apiResponseModel = apiResponseModel;
    }

    public LogResponseModel(String log, String stackTrace, String status, Timestamp startTime, ApiResponseModel<?> apiResponseModel) {
        this.log = log;
        this.stackTrace = stackTrace;
        this.status = status;
        this.startTime = startTime;
        this.apiResponseModel = apiResponseModel;
    }

    public LogResponseModel(String status, ApiResponseModel<?> apiResponseModel)
    {
        this.status = status;
        this.apiResponseModel = apiResponseModel;
    }
}
