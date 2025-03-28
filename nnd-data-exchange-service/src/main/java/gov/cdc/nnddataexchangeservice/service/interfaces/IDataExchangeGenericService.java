package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;

import java.util.List;
import java.util.Map;

public interface IDataExchangeGenericService {
    Integer getTotalRecord(String tableName, boolean initialLoad, String param, boolean keyPagination) throws DataExchangeException;
    String getDataForDataSync(String tableName, String param, String startRow, String endRow,
                            boolean initialLoad, boolean allowNull, boolean noPagination, boolean keyPagination) throws DataExchangeException;
    String decodeAndDecompress(String base64EncodedData) throws DataExchangeException;

    List<Map<String, Object>> getAllTablesCount(String sourceDbName, String tableName, String timestamp, boolean initialLoad) throws DataExchangeException;
}
