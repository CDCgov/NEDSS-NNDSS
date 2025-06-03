package gov.cdc.nnddataexchangeservice.service.interfaces;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.model.dto.CNTransportQOutDto;

import java.util.List;

public interface ICNTransportQOutService {
    List<CNTransportQOutDto> getTransportData(String statusCd, String statusTime, Integer limit) throws DataExchangeException;
}
