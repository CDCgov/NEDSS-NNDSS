package gov.cdc.nnddataexchangeservice.service;

import gov.cdc.nnddataexchangeservice.repository.msg.TransportQOutRepository;
import gov.cdc.nnddataexchangeservice.service.interfaces.ITransportQOutService;
import org.springframework.stereotype.Service;

@Service
public class TransportQOutService implements ITransportQOutService {
    private final TransportQOutRepository transportQOutRepository;

    public TransportQOutService(TransportQOutRepository transportQOutRepository) {
        this.transportQOutRepository = transportQOutRepository;
    }
}
