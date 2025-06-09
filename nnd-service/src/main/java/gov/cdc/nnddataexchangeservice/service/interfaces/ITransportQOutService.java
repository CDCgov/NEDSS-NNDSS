package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.model.dto.TransportQOutDto;

import java.util.List;

public interface ITransportQOutService {
    List<TransportQOutDto> getTransportData(String statusTime, Integer limit) throws DataExchangeException;
}
