package gov.cdc.nnddataexchangeservice.service;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.odse.CNTransportQOutRepository;
import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddataexchangeservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddataexchangeservice.service.model.dto.CNTransportQOutDto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CNTransportQOutService implements ICNTransportQOutService {
    private final CNTransportQOutRepository cnTransportQOutRepository;

    public CNTransportQOutService(CNTransportQOutRepository cnTransportQOutRepository) {
        this.cnTransportQOutRepository = cnTransportQOutRepository;
    }

    public List<CNTransportQOutDto> getTransportData(String statusCd, String statusTime) throws DataExchangeException {

        List<CNTransportQOutDto> cnTransportQOutDtoList = new ArrayList<>();
//        Timestamp timestamp;
        try {
//            if (statusTime == null || statusTime.equals("")) {
//                timestamp = null;
//            } else {
//                timestamp = Timestamp.valueOf(statusTime);
//            }

            Optional<Collection<CNTransportQOut>> transportQOutResults;
            if (statusTime.isEmpty()) {
                transportQOutResults = cnTransportQOutRepository.findTransportByStatusCd(statusCd);
            } else {
                transportQOutResults = cnTransportQOutRepository.findTransportByCreationTimeAndStatus(statusTime, statusCd);
            }

            if (transportQOutResults.isPresent()) {
                for(var item : transportQOutResults.get()) {
                    CNTransportQOutDto transportQOutDto = new CNTransportQOutDto(item);
                    cnTransportQOutDtoList.add(transportQOutDto);
                }
            }

        } catch (Exception e) {
            throw new DataExchangeException(e.getMessage());
        }


        return cnTransportQOutDtoList;
    }
}
