package gov.cdc.nnddatapollservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.json_config.ByteArrayDeserializer;
import gov.cdc.nnddatapollservice.service.interfaces.*;
import gov.cdc.nnddatapollservice.service.model.DataExchangeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataHandlingService implements IDataHandlingService {

    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.endpoint_de}")
    private String exchangeEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ICNTransportQOutService icnTransportQOutService;
    private final INetsstTransportService netsstTransportService;
    private final ITransportQOutService transportQOutService;
    private final ITokenService tokenService;

    public DataHandlingService(ICNTransportQOutService icnTransportQOutService,
                               INetsstTransportService netsstTransportService,
                               ITransportQOutService transportQOutService,
                               ITokenService tokenService) {
        this.icnTransportQOutService = icnTransportQOutService;
        this.netsstTransportService = netsstTransportService;
        this.transportQOutService = transportQOutService;
        this.tokenService = tokenService;
    }

    public void handlingExchangedData() throws DataPollException {
        var cnTimeStamp = icnTransportQOutService.getMaxTimestamp();
        var transportTimeStamp = transportQOutService.getMaxTimestamp();
        var netssTimeStamp = netsstTransportService.getMaxTimestamp();

        var token = tokenService.getToken();

        var param = new HashMap<String, String>();
        param.put("cnStatusTime", cnTimeStamp);
        param.put("transportStatusTime", transportTimeStamp);
        param.put("netssTime", netssTimeStamp);
        param.put("statusCd", "UNPROCESSED");

        String data = callDataExchangeEndpoint(token, param);

         persistingExchangeData(data);
    }

    public void persistingExchangeData(String data) throws DataPollException {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(byte[].class, new ByteArrayDeserializer())
                    .create();
            var dataExchangeModel = gson.fromJson(data, DataExchangeModel.class);

            if (!dataExchangeModel.getCnTransportQOutDtoList().isEmpty()) {
                icnTransportQOutService.saveDataExchange(dataExchangeModel.getCnTransportQOutDtoList());
            }

            if (!dataExchangeModel.getTransportQOutDtoList().isEmpty()) {
                transportQOutService.saveDataExchange(dataExchangeModel.getTransportQOutDtoList());
            }

            if (!dataExchangeModel.getNetssTransportQOutDtoList().isEmpty()) {
                netsstTransportService.saveDataExchange(dataExchangeModel.getNetssTransportQOutDtoList());
            }
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }

    }

    protected String callDataExchangeEndpoint(String token, Map<String, String> params) throws DataPollException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            MultiValueMap<String, String> multiValueParams = new LinkedMultiValueMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                multiValueParams.add(entry.getKey(), entry.getValue());
            }

            URI uri = UriComponentsBuilder.fromHttpUrl(exchangeEndpoint)
                    .queryParams(multiValueParams)
                    .build()
                    .toUri();

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }

    }
}
