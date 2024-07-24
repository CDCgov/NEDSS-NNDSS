package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;

public interface IDataExchangeService {
    DataExchangeModel getDataForOnPremExchanging(String cnStatusTime, String transportStatusTime, String statusCd) throws DataExchangeException;
}
