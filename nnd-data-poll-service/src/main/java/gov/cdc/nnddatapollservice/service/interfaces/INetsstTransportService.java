package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.model.dto.NETSSTransportQOutDto;

import java.util.List;

public interface INetsstTransportService {
    String getMaxTimestamp();
    void saveDataExchange(List<NETSSTransportQOutDto> transportQOutDtoList) throws DataPollException;
    void truncatingData();
}
