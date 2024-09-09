package gov.cdc.nnddatapollservice.rdb.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IRdbDataHandlingService {
    void handlingExchangedData() throws DataPollException;
}
