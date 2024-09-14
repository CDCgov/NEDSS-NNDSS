package gov.cdc.nnddatapollservice.service.data.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IDataHandlingService {
    void handlingExchangedData() throws DataPollException;
    void persistingExchangeData(String data) throws DataPollException;

}