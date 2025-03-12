package gov.cdc.nnddatapollservice.service.model;

import gov.cdc.nnddatapollservice.exception.APIException;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ApiResponseModel<T> {
    private T response; // Generic type, specified when instantiating
    private boolean success;
    private String apiError;

    private String lastApiCall;
    private String lastApiHeader;
    private APIException apiException;

    private Integer lastTotalRecordCount;
    private Integer lastTotalPages;
    private Integer lastBatchSize;
}