package gov.cdc.nnddatapollservice.service.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.model.dto.TransportQOutDto;

import java.util.List;

public interface ITransportQOutService {
    String getMaxTimestamp() throws DataPollException;
    void saveDataExchange(List<TransportQOutDto> transportQOutDtoList) throws DataPollException;
    void truncatingData();
}
