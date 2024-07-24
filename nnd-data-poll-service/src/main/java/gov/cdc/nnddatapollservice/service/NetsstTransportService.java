package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.repository.msg.NETSSTransportQOutRepository;
import gov.cdc.nnddatapollservice.service.interfaces.INetsstTransportService;
import org.springframework.stereotype.Service;

@Service
public class NetsstTransportService implements INetsstTransportService {
    private final NETSSTransportQOutRepository netssTransportQOutRepository;

    public NetsstTransportService(NETSSTransportQOutRepository netssTransportQOutRepository) {
        this.netssTransportQOutRepository = netssTransportQOutRepository;
    }
}
