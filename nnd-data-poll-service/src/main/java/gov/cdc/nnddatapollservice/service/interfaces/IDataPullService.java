package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;

public interface IDataPullService {
    void scheduleNNDDataFetch() throws DataPollException;
    void scheduleDataSync() throws DataPollException, APIException;
}
