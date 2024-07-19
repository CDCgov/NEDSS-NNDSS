package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.repository.msg.TransportQOutRepository;
import gov.cdc.nnddatapollservice.repository.msg.model.TransportQOut;
import gov.cdc.nnddatapollservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddatapollservice.service.interfaces.ITransportQOutService;
import gov.cdc.nnddatapollservice.service.model.dto.TransportQOutDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransportQOutService implements ITransportQOutService {
    private final TransportQOutRepository transportQOutRepository;

    public TransportQOutService(TransportQOutRepository transportQOutRepository) {
        this.transportQOutRepository = transportQOutRepository;
    }

    public String getMaxTimestamp() throws DataPollException {
        try {
            var time = transportQOutRepository.findMaxTimeStamp();
            if (time.isPresent()) {
                return time.get().toString();
            }
            else {
                return "";
            }
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }

    }

    public void saveDataExchange(List<TransportQOutDto> transportQOutDtoList) throws DataPollException {
        try {
            List<TransportQOut> cnTransportQOutList = new ArrayList<>();
            for(var item: transportQOutDtoList) {
                TransportQOut transportQOut = new TransportQOut(item);
                cnTransportQOutList.add(transportQOut);
            }

            transportQOutRepository.saveAll(cnTransportQOutList);
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }
    }

}
