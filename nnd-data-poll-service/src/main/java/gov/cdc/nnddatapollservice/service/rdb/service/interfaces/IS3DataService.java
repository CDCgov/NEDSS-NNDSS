package gov.cdc.nnddatapollservice.service.rdb.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;

import java.sql.Timestamp;
import java.util.List;

public interface IS3DataService {
    String persistToS3MultiPart(String records, String fileName, Timestamp persistingTimestamp, boolean initialLoad) throws DataPollException;
}
