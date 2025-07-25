package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface INNDDataHandlingService {
    void handlingExchangedData() throws DataPollException;
    void handlingNndForModernizedCaseNotification() throws DataPollException;
}
