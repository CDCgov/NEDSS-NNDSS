package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;

public interface IDataExchangeGenericService {
    Integer getTotalRecord(String tableName, boolean initialLoad, String timestamp) throws DataExchangeException;
    String getDataForDataSync(String tableName, String timeStamp, String startRow, String endRow,
                            boolean initialLoad, boolean allowNull) throws DataExchangeException;
    String decodeAndDecompress(String base64EncodedData) throws DataExchangeException;

}
