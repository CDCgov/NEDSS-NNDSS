package gov.cdc.nnddatapollservice.service.data.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IDataPullService {
    void scheduleNNDDataFetch() throws DataPollException;
    void scheduleRDBDataFetch() throws DataPollException;
}
