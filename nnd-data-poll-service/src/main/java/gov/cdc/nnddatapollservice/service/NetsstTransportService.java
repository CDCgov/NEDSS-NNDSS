package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.repository.msg.NETSSTransportQOutRepository;
import gov.cdc.nnddatapollservice.repository.msg.model.NETSSTransportQOut;
import gov.cdc.nnddatapollservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddatapollservice.service.interfaces.IErrorHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.INetsstTransportService;
import gov.cdc.nnddatapollservice.service.model.dto.CNTransportQOutDto;
import gov.cdc.nnddatapollservice.service.model.dto.NETSSTransportQOutDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class NetsstTransportService implements INetsstTransportService {
    private final NETSSTransportQOutRepository netssTransportQOutRepository;
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private final IErrorHandlingService errorHandlingService;

    @Value("${io.finalLocation}")
    private String fileLocation;

    public NetsstTransportService(NETSSTransportQOutRepository netssTransportQOutRepository, IErrorHandlingService errorHandlingService) {
        this.netssTransportQOutRepository = netssTransportQOutRepository;
        this.errorHandlingService = errorHandlingService;
    }

    public void truncatingData() {
        netssTransportQOutRepository.truncateTable();
    }

    public String getMaxTimestamp() {
        var time = netssTransportQOutRepository.findMaxTimeStamp();
        if (time.isPresent()) {
            Timestamp maxTimestamp = time.get();
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            return formatter.format(maxTimestamp);
        }
        else {
            return "";
        }
    }

    public void saveDataExchange(List<NETSSTransportQOutDto> transportQOutDtoList) throws DataPollException {
        try {
            List<NETSSTransportQOut> cnTransportQOutList = new ArrayList<>();
            for (var item : transportQOutDtoList) {
                NETSSTransportQOut transportQOut = new NETSSTransportQOut(item);
                transportQOut.setNetssTransportQOutUid(null);
                cnTransportQOutList.add(transportQOut);
            }

            int batchSize = 10;
            for (int i = 0; i < cnTransportQOutList.size(); i += batchSize) {
                int end = Math.min(i + batchSize, cnTransportQOutList.size());
                List<NETSSTransportQOut> batch = cnTransportQOutList.subList(i, end);

                try {
                    netssTransportQOutRepository.saveAll(batch);
                    netssTransportQOutRepository.flush(); // Ensure the batch is written to the database
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
