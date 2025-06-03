package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataViewConfig;
import gov.cdc.nnddataexchangeservice.service.model.dto.DataViewConfigDto;

import java.util.List;

public interface IDataViewService {
    String getDataForDataView(String queryName, String param, String where) throws DataExchangeException;
    DataViewConfig saveConfig(DataViewConfigDto dataViewConfigDto) throws DataExchangeException;
    List<DataViewConfig> getConfigs(String queryName) throws DataExchangeException;
}
