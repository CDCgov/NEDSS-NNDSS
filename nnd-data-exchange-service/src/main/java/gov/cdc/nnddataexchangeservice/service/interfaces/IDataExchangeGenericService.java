package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;

public interface IDataExchangeGenericService {
    String getGenericDataExchange(String tableName, String timeStamp, Integer limit, boolean nullAllow) throws DataExchangeException;
    String decodeAndDecompress(String base64EncodedData) throws DataExchangeException;
}
