package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;

public interface IDataExchangeGenericService {
    Integer getTotalRecord(String tableName, boolean initialLoad, String param, boolean keyPagination) throws DataExchangeException;
    @SuppressWarnings("java:S107")
    String getDataForDataSync(String tableName, String param, String startRow, String endRow,
                            boolean initialLoad, boolean allowNull, boolean noPagination, boolean keyPagination) throws DataExchangeException;
    String decodeAndDecompress(String base64EncodedData) throws DataExchangeException;

}
