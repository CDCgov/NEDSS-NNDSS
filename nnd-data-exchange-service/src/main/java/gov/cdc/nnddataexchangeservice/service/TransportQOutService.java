package gov.cdc.nnddataexchangeservice.service;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.msg.TransportQOutRepository;
import gov.cdc.nnddataexchangeservice.repository.msg.model.TransportQOut;
import gov.cdc.nnddataexchangeservice.service.interfaces.ITransportQOutService;
import gov.cdc.nnddataexchangeservice.service.model.dto.TransportQOutDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TransportQOutService implements ITransportQOutService {
    private final TransportQOutRepository transportQOutRepository;

    public TransportQOutService(TransportQOutRepository transportQOutRepository) {
        this.transportQOutRepository = transportQOutRepository;
    }

    public List<TransportQOutDto> getTransportData(String statusTime, Integer limit) throws DataExchangeException {

        List<TransportQOutDto> transportQOutDtoList = new ArrayList<>();

        try {

            Optional<Collection<TransportQOut>> transportQOutResults;
            if (statusTime.isEmpty()) {
                if (limit == 0) {
                    transportQOutResults = transportQOutRepository.findTransportByWithoutCreationTime();
                } else {
                    transportQOutResults = transportQOutRepository.findTransportByWithoutCreationTimeWLimit(limit);
                }
            } else {
                if (limit == 0) {
                    transportQOutResults = transportQOutRepository.findTransportByCreationTime(statusTime);
                } else {
                    transportQOutResults = transportQOutRepository.findTransportByCreationTimeWLimit(statusTime, limit);
                }
            }

            if (transportQOutResults.isPresent()) {
                for(var item : transportQOutResults.get()) {
                    TransportQOutDto transportQOutDto = new TransportQOutDto(item);
                    transportQOutDtoList.add(transportQOutDto);
                }
            }
        } catch (Exception e) {
            throw new DataExchangeException(e.getMessage());
        }


        return transportQOutDtoList;
    }
}
