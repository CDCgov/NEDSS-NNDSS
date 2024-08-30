package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IDataPullService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataPullService implements IDataPullService {
    private static Logger logger = LoggerFactory.getLogger(DataPullService.class);

    private final IDataHandlingService dataHandlingService;
    private final IRdbDataHandlingService rdbDataHandlingService;
    @Value("${scheduler.cron}")
    private String cron;


    @Value("${scheduler.zone}")
    private String zone;

    public DataPullService(IDataHandlingService dataHandlingService,
                           IRdbDataHandlingService rdbDataHandlingService) {
        this.dataHandlingService = dataHandlingService;
        this.rdbDataHandlingService = rdbDataHandlingService;
    }


    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    public void scheduleDataFetch() throws DataPollException {
        logger.info("CRON STARTED");
        logger.info(cron);
        logger.info(zone);
        dataHandlingService.handlingExchangedData();
        rdbDataHandlingService.handlingExchangedData();
    }
}