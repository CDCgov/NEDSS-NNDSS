package gov.cdc.nnddatapollservice.srte.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface ISrteDataHandlingService {
    void handlingExchangedData() throws DataPollException;
}
