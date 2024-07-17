package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.repository.odse.CNTransportQOutRepository;
import gov.cdc.nnddatapollservice.service.interfaces.ICNTransportQOutService;
import org.springframework.stereotype.Service;

@Service
public class CNTransportQOutService implements ICNTransportQOutService {
    private final CNTransportQOutRepository cnTransportQOutRepository;

    public CNTransportQOutService(CNTransportQOutRepository cnTransportQOutRepository) {
        this.cnTransportQOutRepository = cnTransportQOutRepository;
    }
}
