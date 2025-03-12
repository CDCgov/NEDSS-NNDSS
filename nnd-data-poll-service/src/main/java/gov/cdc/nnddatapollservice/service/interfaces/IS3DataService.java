package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;

import java.sql.Timestamp;

public interface IS3DataService {
    LogResponseModel persistToS3MultiPart(String domain, String records, String fileName,
                                          Timestamp persistingTimestamp, boolean initialLoad,
                                          ApiResponseModel<?> apiResponseModel) ;
}
