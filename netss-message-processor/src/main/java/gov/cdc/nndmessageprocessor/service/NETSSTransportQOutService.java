package gov.cdc.nndmessageprocessor.service;

import gov.cdc.nndmessageprocessor.exception.DataProcessorException;
import gov.cdc.nndmessageprocessor.repository.NetssTransportQOutRepository;
import gov.cdc.nndmessageprocessor.service.interfaces.INETSSTransportQOutService;
import gov.cdc.nndmessageprocessor.service.model.dto.NETSSTransportQOutDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NETSSTransportQOutService implements INETSSTransportQOutService {
    private final NetssTransportQOutRepository netssTransportQOutRepository;

    public NETSSTransportQOutService(NetssTransportQOutRepository netssTransportQOutRepository) {
        this.netssTransportQOutRepository = netssTransportQOutRepository;
    }

    public List<NETSSTransportQOutDto> getNetssCaseDataYtdAndPriorYear(int currentYear, int currentWeek, int priorYear) throws DataProcessorException {
        try {
            List<NETSSTransportQOutDto> lst = new ArrayList<>();
            var res  = netssTransportQOutRepository.findNetssCaseDataYtdAndPriorYear(currentYear, currentWeek, priorYear);
            if (res.isPresent()) {
                for(var item : res.get()) {
                    lst.add(new NETSSTransportQOutDto(item));
                }
            }
            return lst;
        } catch (Exception e) {
            throw new DataProcessorException(e.getMessage());
        }

    }

    public List<NETSSTransportQOutDto> getNetssCaseDataYtd(int currentYear, int currentWeek) throws DataProcessorException {
        try {
            List<NETSSTransportQOutDto> lst = new ArrayList<>();
            var res =  netssTransportQOutRepository.findNetssCaseDataYtd(currentYear, currentWeek);
            if (res.isPresent()) {
                for(var item : res.get()) {
                    lst.add(new NETSSTransportQOutDto(item));
                }
            }
            return lst;
        } catch (Exception e) {
            throw new DataProcessorException(e.getMessage());
        }
    }
}
