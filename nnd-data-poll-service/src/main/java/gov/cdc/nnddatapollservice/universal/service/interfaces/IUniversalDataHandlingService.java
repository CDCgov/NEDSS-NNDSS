package gov.cdc.nnddatapollservice.universal.service.interfaces;

import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IUniversalDataHandlingService {
    void handlingExchangedData(String source) throws DataPollException, APIException;
}
