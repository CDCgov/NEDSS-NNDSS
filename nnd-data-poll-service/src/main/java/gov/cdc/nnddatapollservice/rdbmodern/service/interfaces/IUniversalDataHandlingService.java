package gov.cdc.nnddatapollservice.rdbmodern.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IUniversalDataHandlingService {
    void handlingExchangedData(String source) throws DataPollException;
}
