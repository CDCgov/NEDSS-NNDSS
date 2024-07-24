package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IDataPullService {
    void scheduleDataFetch() throws DataPollException;
}
