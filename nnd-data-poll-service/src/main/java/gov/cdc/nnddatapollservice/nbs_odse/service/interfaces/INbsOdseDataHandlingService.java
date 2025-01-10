package gov.cdc.nnddatapollservice.nbs_odse.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface INbsOdseDataHandlingService {
    void handlingExchangedData() throws DataPollException;
}
