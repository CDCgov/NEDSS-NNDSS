package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.repository.msg.TransportQOutRepository;
import gov.cdc.nnddatapollservice.service.interfaces.ITransportQOutService;
import org.springframework.stereotype.Service;

@Service
public class TransportQOutService implements ITransportQOutService {
    private final TransportQOutRepository transportQOutRepository;

    public TransportQOutService(TransportQOutRepository transportQOutRepository) {
        this.transportQOutRepository = transportQOutRepository;
    }
}
