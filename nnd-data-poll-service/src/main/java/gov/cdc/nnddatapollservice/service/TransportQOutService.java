package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.repository.msg.TransportQOutRepository;
import gov.cdc.nnddatapollservice.repository.msg.model.TransportQOut;
import gov.cdc.nnddatapollservice.service.interfaces.IErrorHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.ITransportQOutService;
import gov.cdc.nnddatapollservice.service.model.dto.TransportQOutDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransportQOutService implements ITransportQOutService {
    private final TransportQOutRepository transportQOutRepository;
    private final IErrorHandlingService errorHandlingService;

    @Value("${io.finalLocation}")
    private String fileLocation;

    public TransportQOutService(TransportQOutRepository transportQOutRepository, IErrorHandlingService errorHandlingService) {
        this.transportQOutRepository = transportQOutRepository;
        this.errorHandlingService = errorHandlingService;
    }

    public String getMaxTimestamp() throws DataPollException {
        try {
            var time = transportQOutRepository.findMaxTimeStamp();
            if (time.isPresent()) {
                return time.get().toString();
            }
            else {
                return "";
            }
        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }

    }

    public void saveDataExchange(List<TransportQOutDto> transportQOutDtoList) throws DataPollException {
        try {
            List<TransportQOut> cnTransportQOutList = new ArrayList<>();
            for (var item : transportQOutDtoList) {
                TransportQOut transportQOut = new TransportQOut(item);
                transportQOut.setRecordId(null);
                cnTransportQOutList.add(transportQOut);
            }

            int batchSize = 10;
            for (int i = 0; i < cnTransportQOutList.size(); i += batchSize) {
                int end = Math.min(i + batchSize, cnTransportQOutList.size());
                List<TransportQOut> batch = cnTransportQOutList.subList(i, end);

                try {
                    transportQOutRepository.saveAll(batch);
                    transportQOutRepository.flush(); // Ensure the batch is written to the database
                }  catch (Exception e) {
                    // Log the error and dump the batch to a file
                    String fileName = "transportQOut_failed_batch_" + System.currentTimeMillis() + ".json";
                    errorHandlingService.dumpBatchToFile(batch, fileName, fileLocation);
                }
            }

        } catch (Exception e) {
            throw new DataPollException(e.getMessage());
        }
    }

}
