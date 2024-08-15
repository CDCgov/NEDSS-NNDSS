package gov.cdc.nnddatapollservice.service;

import com.google.gson.Gson;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.*;
import gov.cdc.nnddatapollservice.service.model.DataExchangeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataPullService implements IDataPullService {
    private static Logger logger = LoggerFactory.getLogger(DataPullService.class);

    private final IDataHandlingService dataHandlingService;

    @Value("${scheduler.cron}")
    private String cron;


    @Value("${scheduler.zone}")
    private String zone;

    public DataPullService(IDataHandlingService dataHandlingService) {
        this.dataHandlingService = dataHandlingService;


    }


    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")

    public void scheduleDataFetch() throws DataPollException {
        logger.info("CRON STARTED");
        logger.info(cron);
        logger.info(zone);
        dataHandlingService.handlingExchangedData();
    }



}
