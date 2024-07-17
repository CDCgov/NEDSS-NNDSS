package gov.cdc.nnddataexchangeservice.service;

import gov.cdc.nnddataexchangeservice.repository.msg.NETSSTransportQOutRepository;
import gov.cdc.nnddataexchangeservice.repository.msg.TransportQOutRepository;
import gov.cdc.nnddataexchangeservice.service.interfaces.INetsstTransportService;
import org.springframework.stereotype.Service;

@Service
public class NetsstTransportService implements INetsstTransportService {
    private final NETSSTransportQOutRepository netssTransportQOutRepository;

    public NetsstTransportService(NETSSTransportQOutRepository netssTransportQOutRepository) {
        this.netssTransportQOutRepository = netssTransportQOutRepository;
    }
}
