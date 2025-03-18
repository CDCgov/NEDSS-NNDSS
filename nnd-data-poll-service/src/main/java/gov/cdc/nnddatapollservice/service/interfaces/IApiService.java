package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;

public interface IApiService {
    ApiResponseModel<Integer> callDataCountEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime, boolean useKeyPagination, String entityKey);

    ApiResponseModel<String> callDataExchangeEndpoint(String tableName, boolean isInitialLoad, String lastUpdatedTime, boolean allowNull,
                                              String startRow, String endRow, boolean noPagination, boolean useKeyPagination, String entityKey);
}
