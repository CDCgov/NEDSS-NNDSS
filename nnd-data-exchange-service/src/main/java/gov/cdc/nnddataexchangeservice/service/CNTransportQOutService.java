package gov.cdc.nnddataexchangeservice.service;

import gov.cdc.nnddataexchangeservice.repository.odse.CNTransportQOutRepository;
import gov.cdc.nnddataexchangeservice.service.interfaces.ICNTransportQOutService;
import org.springframework.stereotype.Service;

@Service
public class CNTransportQOutService implements ICNTransportQOutService {
    private final CNTransportQOutRepository cnTransportQOutRepository;

    public CNTransportQOutService(CNTransportQOutRepository cnTransportQOutRepository) {
        this.cnTransportQOutRepository = cnTransportQOutRepository;
    }
}
