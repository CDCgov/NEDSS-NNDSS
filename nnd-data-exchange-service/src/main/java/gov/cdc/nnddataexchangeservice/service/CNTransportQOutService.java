package gov.cdc.nnddataexchangeservice.service;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.odse.CNTransportQOutRepository;
import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddataexchangeservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddataexchangeservice.service.model.dto.CNTransportQOutDto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CNTransportQOutService implements ICNTransportQOutService {
    private final CNTransportQOutRepository cnTransportQOutRepository;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public CNTransportQOutService(CNTransportQOutRepository cnTransportQOutRepository) {
        this.cnTransportQOutRepository = cnTransportQOutRepository;
    }

    public List<CNTransportQOutDto> getTransportData(String statusCd, String statusTime, Integer limit) throws DataExchangeException {

        List<CNTransportQOutDto> cnTransportQOutDtoList = new ArrayList<>();
        try {

            Optional<Collection<CNTransportQOut>> transportQOutResults;
            if (statusTime.isEmpty()) {
                if (limit == 0) {
                    transportQOutResults = cnTransportQOutRepository.findTransportByStatusCd(statusCd);
                } else {
                    transportQOutResults = cnTransportQOutRepository.findTransportByStatusCdWLimit(statusCd, limit);

                }
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
                java.util.Date parsedDate = formatter.parse(statusTime);
                Timestamp recordStatusTime = new Timestamp(parsedDate.getTime());
                if (limit == 0) {
                    transportQOutResults = cnTransportQOutRepository.findTransportByCreationTimeAndStatus(statusTime, statusCd);
                } else {
                    transportQOutResults = cnTransportQOutRepository.findTransportByCreationTimeAndStatusWLimit(recordStatusTime, statusCd, limit);
                }
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
