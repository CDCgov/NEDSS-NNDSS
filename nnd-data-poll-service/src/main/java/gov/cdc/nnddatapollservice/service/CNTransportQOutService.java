package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.repository.odse.CNTransportQOutRepository;
import gov.cdc.nnddatapollservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddatapollservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddatapollservice.service.model.dto.CNTransportQOutDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CNTransportQOutService implements ICNTransportQOutService {
    private final CNTransportQOutRepository cnTransportQOutRepository;

    public CNTransportQOutService(CNTransportQOutRepository cnTransportQOutRepository) {
        this.cnTransportQOutRepository = cnTransportQOutRepository;
    }

    public String getMaxTimestamp() {
        var time = cnTransportQOutRepository.findMaxTimeStamp();
        if (time.isPresent()) {
            return time.get().toString();
        }
        else {
            return "";
        }
    }

    public void saveDataExchange(List<CNTransportQOutDto> transportQOutDtoList) throws DataPollException {
        try {
            List<CNTransportQOut> cnTransportQOutList = new ArrayList<>();
            for(var item: transportQOutDtoList) {
                CNTransportQOut transportQOut = new CNTransportQOut(item);
                cnTransportQOutList.add(transportQOut);
            }

            cnTransportQOutRepository.saveAll(cnTransportQOutList);
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }

    }
}
