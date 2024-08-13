package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;

import java.io.IOException;

public interface IDataExchangeGenericService {
    String getGenericDataExchange(String tableName, String timeStamp) throws DataExchangeException;
    String decodeAndDecompress(String base64EncodedData);
}
