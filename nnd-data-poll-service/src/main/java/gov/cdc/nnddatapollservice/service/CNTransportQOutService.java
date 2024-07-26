package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.repository.odse.CNTransportQOutRepository;
import gov.cdc.nnddatapollservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddatapollservice.service.interfaces.ICNTransportQOutService;
import gov.cdc.nnddatapollservice.service.interfaces.IErrorHandlingService;
import gov.cdc.nnddatapollservice.service.model.dto.CNTransportQOutDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CNTransportQOutService implements ICNTransportQOutService {
    private final CNTransportQOutRepository cnTransportQOutRepository;
    private final IErrorHandlingService errorHandlingService;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Value("${io.finalLocation}")
    private String fileLocation;


    public CNTransportQOutService(CNTransportQOutRepository cnTransportQOutRepository,
                                  IErrorHandlingService errorHandlingService) {
        this.cnTransportQOutRepository = cnTransportQOutRepository;
        this.errorHandlingService = errorHandlingService;
    }

    public String getMaxTimestamp() {
        var time = cnTransportQOutRepository.findMaxTimeStamp();
        if (time.isPresent()) {
            Timestamp maxTimestamp = time.get();
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            return formatter.format(maxTimestamp);
        }
        else {
            return "";
        }
    }

    public void saveDataExchange(List<CNTransportQOutDto> transportQOutDtoList) throws DataPollException {
        try {
            List<CNTransportQOut> cnTransportQOutList = new ArrayList<>();
            for (var item : transportQOutDtoList) {
                CNTransportQOut transportQOut = new CNTransportQOut(item);
                cnTransportQOutList.add(transportQOut);
            }

            int batchSize = 10;
            for (int i = 0; i < cnTransportQOutList.size(); i += batchSize) {
                int end = Math.min(i + batchSize, cnTransportQOutList.size());
                List<CNTransportQOut> batch = cnTransportQOutList.subList(i, end);

                try {
                    cnTransportQOutRepository.saveAll(batch);
                    cnTransportQOutRepository.flush(); // Ensure the batch is written to the database
                } catch (Exception e) {
                    // Log the error and dump the batch to a file
                    String fileName = "cn_transportQOut_failed_batch_" + System.currentTimeMillis() + ".json";
                    errorHandlingService.dumpBatchToFile(batch, fileName, fileLocation);
                }
            }

        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }

    }
}
