package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.model.dto.NETSSTransportQOutDto;

import java.util.List;

public interface INetsstTransportService {
    List<NETSSTransportQOutDto> getNetssTransportData(String statusTime, Integer limit) throws DataExchangeException;
}
