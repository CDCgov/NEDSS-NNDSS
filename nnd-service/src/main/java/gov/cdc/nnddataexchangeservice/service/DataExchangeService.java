package gov.cdc.nnddataexchangeservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddataexchangeservice.configuration.TimestampAdapter;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeService;
import gov.cdc.nnddataexchangeservice.service.interfaces.INetsstTransportService;
import gov.cdc.nnddataexchangeservice.service.interfaces.ITransportQOutService;
import gov.cdc.nnddataexchangeservice.service.model.DataExchangeModel;
import gov.cdc.nnddataexchangeservice.shared.DataSimplification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
public class DataExchangeService implements IDataExchangeService {
    private final INetsstTransportService netsstTransportService;
    private final ITransportQOutService transportQOutService;
    private final ICNTransportQOutService cnTransportQOutService;

    private final Gson gson;

    public DataExchangeService(INetsstTransportService netsstTransportService,
                               ITransportQOutService transportQOutService,
                               ICNTransportQOutService icnTransportQOutService) {
        this.netsstTransportService = netsstTransportService;
        this.transportQOutService = transportQOutService;
        this.cnTransportQOutService = icnTransportQOutService;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
                .serializeNulls()
                .create();
    }

    public String getDataForOnPremExchanging(String cnStatusTime, String transportTime,
                                                        String netssTime, String statusCd,
                                                        Integer limit, boolean compressionApplied) throws DataExchangeException, IOException {
        var dataExchange = new DataExchangeModel();
        var cnTransportDatas = cnTransportQOutService.getTransportData(statusCd, cnStatusTime, limit);
        var transportDatas = transportQOutService.getTransportData(transportTime, limit);
        var netssDatas = netsstTransportService.getNetssTransportData(netssTime, limit);

        dataExchange.setCnTransportQOutDtoList(cnTransportDatas);
        dataExchange.setTransportQOutDtoList(transportDatas);
        dataExchange.setNetssTransportQOutDtoList(netssDatas);

        dataExchange.setCountCnTransport(cnTransportDatas.size());
        dataExchange.setCountTransport(transportDatas.size());
        dataExchange.setCountNetssTransport(netssDatas.size());
        String strData = gson.toJson(dataExchange);
        String finalData;
        if (compressionApplied) {
            finalData = DataSimplification.dataCompressionAndEncode(strData);
        } else {
            finalData = strData;
        }
        return finalData;
    }
}
