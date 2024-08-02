package gov.cdc.nndmessageprocessor.service.interfaces;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.service.model.dto.NETSSTransportQOutDto;

import java.util.List;

public interface INETSSTransportQOutService {
    List<NETSSTransportQOutDto> getNetssCaseDataYtdAndPriorYear(int currentYear, int currentWeek, int priorYear) throws DataProcessorException;
    List<NETSSTransportQOutDto> getNetssCaseDataYtd(int currentYear, int currentWeek) throws DataProcessorException;
}
