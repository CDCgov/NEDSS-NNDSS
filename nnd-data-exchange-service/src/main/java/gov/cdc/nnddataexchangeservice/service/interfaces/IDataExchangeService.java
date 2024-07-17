package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;

public interface IDataExchangeService {
    DataExchangeModel getDataForOnPremExchanging();
}
