package gov.cdc.nnddatapollservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.configuration.HttpClientProvider;
import gov.cdc.nnddatapollservice.configuration.TimestampAdapter;
import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.*;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.DataExchangeModel;
import gov.cdc.nnddatapollservice.share.DataSimplification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

import static gov.cdc.nnddatapollservice.constant.ApiConstantValue.CLIENT_ID;
import static gov.cdc.nnddatapollservice.constant.ApiConstantValue.CLIENT_SECRET;

@Service
public class NNDDataHandlingServiceWithRetry implements INNDDataHandlingService {
    private static final Logger logger = LoggerFactory.getLogger(NNDDataHandlingService.class);

    @Value("${data_exchange.clientId}")
    private String clientId;

    @Value("${data_exchange.secret}")
    private String clientSecret;

    @Value("${data_exchange.endpoint_de}")
    protected String exchangeEndpoint;

    @Value("${nnd.fullLoad}")
    protected boolean fullLoadApplied;

    @Value("${nnd.pullLimit}")
    private String pullLimit;

    @Value("${data_exchange.retry.max-attempts:5}")
    private int maxRetries;

    @Value("${data_exchange.retry.delay-ms:3000}")
    private long retryDelayMs;

    @Value("${data_exchange.retry.stuck-threshold-minutes:10}")
    private int stuckThresholdMinutes;

    private final HttpClient httpClient;
    private final Gson gson;
    private final ITokenService tokenService;
    private final RetriableRequestExecutor retrier;
    private final ICNTransportQOutService icnTransportQOutService;
    private final INetsstTransportService netsstTransportService;
    private final ITransportQOutService transportQOutService;
    private final Semaphore semaphore = new Semaphore(20);

    public NNDDataHandlingServiceWithRetry(ICNTransportQOutService icnTransportQOutService,
                                           INetsstTransportService netsstTransportService,
                                           ITransportQOutService transportQOutService,
                                           ITokenService tokenService,
                                           RetriableRequestExecutor retrier) {
        this.httpClient = HttpClientProvider.getInstance();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
                .create();
        this.icnTransportQOutService = icnTransportQOutService;
        this.netsstTransportService = netsstTransportService;
        this.transportQOutService = transportQOutService;
        this.tokenService = tokenService;
        this.retrier = retrier;
    }

    public void handlingExchangedData() throws DataPollException {
        truncatingDataForFullLoading();
        var cnTimeStamp = icnTransportQOutService.getMaxTimestamp();
        var transportTimeStamp = transportQOutService.getMaxTimestamp();
        var netssTimeStamp = netsstTransportService.getMaxTimestamp();

        var param = new HashMap<String, String>();
        param.put("cnStatusTime", cnTimeStamp);
        param.put("transportStatusTime", transportTimeStamp);
        param.put("netssTime", netssTimeStamp);
        param.put("statusCd", "UNPROCESSED");

        String data = callDataExchangeEndpoint(param);
        var deCompressedData = DataSimplification.decodeAndDecompress(data);

        int index = deCompressedData.lastIndexOf("],") + 2;
        String countLoggingString = "{" + deCompressedData.substring(index).trim();
        logger.info("Decompressed Data count is: {}", countLoggingString);

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

    protected String callDataExchangeEndpoint(Map<String, String> params) throws DataPollException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CLIENT_ID, clientId);
        headers.add(CLIENT_SECRET, clientSecret);
        headers.add("compress", "true");
        headers.add("limit", pullLimit);

        URI uri = UriComponentsBuilder.fromHttpUrl(exchangeEndpoint)
                .queryParams(CollectionUtils.toMultiValueMap(convertToMultiValueMap(params)))
                .build()
                .toUri();

        logger.info("Data Exchange API URL: {} , headers: {}", uri, headers);

        ApiResponseModel<String> result = retrier.executeWithRetry(
                () -> {
                    String token = tokenService.getToken();
                    headers.setBearerAuth(token);
                    return buildRequest(uri, headers, Duration.ofSeconds(120));
                },
                response -> {
                    ApiResponseModel<String> model = new ApiResponseModel<>();
                    if (response.statusCode() == 200) {
                        model.setResponse(response.body());
                        model.setSuccess(true);
                    } else {
                        model.setSuccess(false);
                        model.setApiException(new APIException("Unexpected status code: " + response.statusCode()));
                    }
                    return model;
                },
                ignore -> tokenService.getToken(),
                "Data Exchange API Call",
                maxRetries,
                retryDelayMs,
                Duration.ofMinutes(stuckThresholdMinutes),
                semaphore,
                httpClient,
                false
        );

        if (!result.isSuccess()) {
            throw new DataPollException(result.getApiException().getMessage());
        }

        return result.getResponse();
    }


    private HttpRequest buildRequest(URI uri, HttpHeaders headers, Duration timeout) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .headers(headers.entrySet().stream()
                        .flatMap(e -> Stream.of(e.getKey(), e.getValue().get(0)))
                        .toArray(String[]::new))
                .timeout(timeout)
                .build();
    }

    private MultiValueMap<String, String> convertToMultiValueMap(Map<String, String> paramMap) {
        MultiValueMap<String, String> multiValueMap = new org.springframework.util.LinkedMultiValueMap<>();
        paramMap.forEach(multiValueMap::add);
        return multiValueMap;
    }
}