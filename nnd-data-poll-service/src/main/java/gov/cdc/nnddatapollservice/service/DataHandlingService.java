package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.service.interfaces.IDataHandlingService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataHandlingService implements IDataHandlingService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String dataExchangeApiUrl = "";

    public void handlingExchangedData() {
        String data = restTemplate.getForObject(dataExchangeApiUrl, String.class);

    }
}
