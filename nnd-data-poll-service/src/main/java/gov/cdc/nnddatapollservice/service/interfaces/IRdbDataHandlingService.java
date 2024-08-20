package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IRdbDataHandlingService {
    void handlingExchangedData(String tableName) throws DataPollException;
    void persistRdbData(String tableName, String jsonData) throws DataPollException;
}
