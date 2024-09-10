package gov.cdc.nnddatapollservice.service.nnd.interfaces;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.data.model.dto.CNTransportQOutDto;

import java.util.List;

public interface ICNTransportQOutService {
    String getMaxTimestamp();
    void saveDataExchange(List<CNTransportQOutDto> transportQOutDtoList) throws DataPollException;
    void truncatingData();
}
