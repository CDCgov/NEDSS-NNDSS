package gov.cdc.nnddataexchangeservice.service;

import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.msg.NETSSTransportQOutRepository;
import gov.cdc.nnddataexchangeservice.repository.msg.model.NETSSTransportQOut;
import gov.cdc.nnddataexchangeservice.service.interfaces.INetsstTransportService;
import gov.cdc.nnddataexchangeservice.service.model.dto.NETSSTransportQOutDto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class NetsstTransportService implements INetsstTransportService {
    private final NETSSTransportQOutRepository netssTransportQOutRepository;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public NetsstTransportService(NETSSTransportQOutRepository netssTransportQOutRepository) {
        this.netssTransportQOutRepository = netssTransportQOutRepository;
    }


    public List<NETSSTransportQOutDto> getNetssTransportData(String statusTime, Integer limit) throws DataExchangeException {

        List<NETSSTransportQOutDto> cnTransportQOutDtoList = new ArrayList<>();
        try {

            Optional<Collection<NETSSTransportQOut>> transportQOutResults;
            if (statusTime.isEmpty()) {
                if (limit == 0) {
                    transportQOutResults = netssTransportQOutRepository.findNetssTransport();
                } else {
                    transportQOutResults = netssTransportQOutRepository.findNetssTransportWLimit(limit);
                }
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
                java.util.Date parsedDate = formatter.parse(statusTime);
                Timestamp recordStatusTime = new Timestamp(parsedDate.getTime());
                if (limit == 0) {
                    transportQOutResults = netssTransportQOutRepository.findNetssTransportByCreationTime(recordStatusTime);
                } else {
                    transportQOutResults = netssTransportQOutRepository.findNetssTransportByCreationTimeWLimit(recordStatusTime, limit);
                }
            }

            if (transportQOutResults.isPresent()) {
                for(var item : transportQOutResults.get()) {
                    NETSSTransportQOutDto transportQOutDto = new NETSSTransportQOutDto(item);
                    cnTransportQOutDtoList.add(transportQOutDto);
                }
            }

        } catch (Exception e) {
            throw new DataExchangeException(e.getMessage());
        }


        return cnTransportQOutDtoList;
    }
}
