package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.INNDDataHandlingService;
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

    @Value("${scheduler.cron}")
    private String cron;

    @Value("${scheduler.zone}")
    private String zone;

    @Value("${nnd.poll.enabled}")
    private boolean nndPollEnabled;

    @Value("${rdb.poll.enabled}")
    private boolean rdbPollEnabled;

    private final INNDDataHandlingService dataHandlingService;
    private final IRdbDataHandlingService rdbDataHandlingService;

    public DataPullService(INNDDataHandlingService dataHandlingService,
                           IRdbDataHandlingService rdbDataHandlingService) {
        this.dataHandlingService = dataHandlingService;
        this.rdbDataHandlingService = rdbDataHandlingService;
    }

    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    public void scheduleNNDDataFetch() throws DataPollException {
        if (nndPollEnabled) {
            logger.info("CRON STARTED");
            logger.info(cron);
            logger.info(zone);
            dataHandlingService.handlingExchangedData();
        }
    }

    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    public void scheduleRDBDataFetch() throws DataPollException {
        if (rdbPollEnabled) {
            logger.info("CRON STARTED FOR POLLING RDB");
            logger.info("{}, {} FOR RDB",cron,zone);
            rdbDataHandlingService.handlingExchangedData();
        }
    }
}