package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;

import java.io.IOException;

public interface IDataExchangeService {
    String getDataForOnPremExchanging(String cnStatusTime, String transportStatusTime,
                                                 String netssTime , String statusCd,
                                                 Integer limit, boolean compressionApplied) throws DataExchangeException, IOException;
}
