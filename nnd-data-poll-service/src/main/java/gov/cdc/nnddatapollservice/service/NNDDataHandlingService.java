package gov.cdc.nnddatapollservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.*;
import gov.cdc.nnddatapollservice.service.model.DataExchangeModel;
import gov.cdc.nnddatapollservice.share.DataSimplification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class NNDDataHandlingService implements INNDDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(NNDDataHandlingService.class);

    @Value("${data_exchange.clientId}")
    private String clientId = "clientId";

    @Value("${data_exchange.secret}")
    private String clientSecret = "clientSecret";

    @Value("${data_exchange.endpoint_de}")
    protected String exchangeEndpoint;

    @Value("${nnd.fullLoad}")
    protected boolean fullLoadApplied = false;

    @Value("${nnd.pullLimit}")
    private String pullLimit = "0";

    private final Gson gson;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ICNTransportQOutService icnTransportQOutService;
    private final INetsstTransportService netsstTransportService;
    private final ITransportQOutService transportQOutService;
    private final ITokenService tokenService;

    public NNDDataHandlingService(ICNTransportQOutService icnTransportQOutService,
                                  INetsstTransportService netsstTransportService,
                                  ITransportQOutService transportQOutService,
                                  ITokenService tokenService) {
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .create();
        this.icnTransportQOutService = icnTransportQOutService;
        this.netsstTransportService = netsstTransportService;
        this.transportQOutService = transportQOutService;
        this.tokenService = tokenService;
    }

    public void handlingExchangedData() throws DataPollException {
        truncatingDataForFullLoading();
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

        var deCompressedData = DataSimplification.decodeAndDecompress(data);
        logger.info("Decompress Data: {}", deCompressedData);

        persistingExchangeData(deCompressedData);
    }

    public void persistingExchangeData(String data) throws DataPollException {
        try {
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

    protected void truncatingDataForFullLoading() {
        if (fullLoadApplied) {
            icnTransportQOutService.truncatingData();
            transportQOutService.truncatingData();
            netsstTransportService.truncatingData();
        }

    }

    protected String callDataExchangeEndpoint(String token, Map<String, String> params) throws DataPollException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.add("clientid", clientId);
            headers.add("clientsecret", clientSecret);
            headers.add("compress", "true");
            headers.add("limit", pullLimit);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            MultiValueMap<String, String> multiValueParams = new LinkedMultiValueMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                multiValueParams.add(entry.getKey(), entry.getValue());
            }

            URI uri = UriComponentsBuilder.fromHttpUrl(exchangeEndpoint)
                    .queryParams(multiValueParams)
                    .build()
                    .toUri();

            logger.info("API Request: {}", uri);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }

    }
}