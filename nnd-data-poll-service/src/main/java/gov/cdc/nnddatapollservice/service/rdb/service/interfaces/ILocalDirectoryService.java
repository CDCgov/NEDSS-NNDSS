package gov.cdc.nnddatapollservice.service.rdb.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

import java.sql.Timestamp;

public interface ILocalDirectoryService {
    String persistToLocalDirectory(String records, String fileName, Timestamp persistingTimestamp, boolean initialLoad) throws DataPollException;
}
