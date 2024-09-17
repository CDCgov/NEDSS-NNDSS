package gov.cdc.nnddatapollservice.rdbmodern.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IRdbModernDataHandlingService {
    void handlingExchangedData() throws DataPollException;
}
