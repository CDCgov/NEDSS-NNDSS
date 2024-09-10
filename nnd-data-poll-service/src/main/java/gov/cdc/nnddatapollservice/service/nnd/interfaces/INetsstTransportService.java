package gov.cdc.nnddatapollservice.service.nnd.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.data.model.dto.NETSSTransportQOutDto;

import java.util.List;

public interface INetsstTransportService {
    String getMaxTimestamp();
    void saveDataExchange(List<NETSSTransportQOutDto> transportQOutDtoList) throws DataPollException;
    void truncatingData();
}
