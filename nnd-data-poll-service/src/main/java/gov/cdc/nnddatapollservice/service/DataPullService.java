package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.rdb.service.interfaces.IRdbDataHandlingService;
import gov.cdc.nnddatapollservice.rdbmodern.service.interfaces.IRdbModernDataHandlingService;
import gov.cdc.nnddatapollservice.service.interfaces.IDataPullService;
import gov.cdc.nnddatapollservice.service.interfaces.INNDDataHandlingService;
import gov.cdc.nnddatapollservice.srte.service.interfaces.ISrteDataHandlingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataPullService implements IDataPullService {

    private static final Logger logger = LoggerFactory.getLogger(DataPullService.class);

    @Value("${scheduler.cron}")
    private String cron;
    @Value("${scheduler.zone}")
    private String zone;

    @Value("${poll.nnd.enabled}")
    private boolean nndPollEnabled;
    @Value("${poll.rdb.enabled}")
    private boolean rdbPollEnabled;
    @Value("${poll.rdb_modern.enabled}")
    private boolean rdbModernPollEnabled;
    @Value("${poll.srte.enabled}")
    private boolean srtePollEnabled;

    @Value("${poll.single_time_poll_enabled}")
    private boolean singlePoll;

    @Value("${datasync.sql_reprocessing_data}")
    private boolean reprocessFailedSQL = false;

    private final INNDDataHandlingService dataHandlingService;
    private final IRdbDataHandlingService rdbDataHandlingService;
    private final IRdbModernDataHandlingService rdbModernDataHandlingService;
    private final ISrteDataHandlingService srteDataHandlingService;

    public DataPullService(INNDDataHandlingService dataHandlingService,
                           IRdbDataHandlingService rdbDataHandlingService,
                           IRdbModernDataHandlingService rdbModernDataHandlingService,
                           ISrteDataHandlingService srteDataHandlingService) {
        this.dataHandlingService = dataHandlingService;
        this.rdbDataHandlingService = rdbDataHandlingService;
        this.rdbModernDataHandlingService = rdbModernDataHandlingService;
        this.srteDataHandlingService = srteDataHandlingService;
    }

    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    public void scheduleNNDDataFetch() throws DataPollException {
    if (nndPollEnabled) {
        logger.info("CRON STARTED FOR NND");
        logger.info(cron);
        logger.info(zone);
        dataHandlingService.handlingExchangedData();
        closePoller();
    }
}

    @Scheduled(cron = "${scheduler.cron_rdb}", zone = "${scheduler.zone}")
    public void scheduleRDBDataFetch() throws DataPollException {
        if (rdbPollEnabled) {
            logger.info("CRON STARTED FOR POLLING RDB");
            logger.info("{}, {} FOR RDB", cron, zone);
            rdbDataHandlingService.handlingExchangedData();

            // RDB MODERN and SRTE are now part of RDB
            rdbModernDataHandlingService.handlingExchangedData();
            srteDataHandlingService.handlingExchangedData();
            logger.info("CRON ENDED FOR POLLING RDB");
            closePoller();
        }
    }

    @Scheduled(cron = "${scheduler.cron_rdb_modern}", zone = "${scheduler.zone}")
    public void scheduleRdbModernDataFetch() throws DataPollException {
        if (rdbModernPollEnabled) {
            logger.info("CRON STARTED FOR POLLING RDB_MODERN");
            logger.info("{}, {} FOR RDB_MODERN", cron, zone);
            rdbModernDataHandlingService.handlingExchangedData();
            closePoller();
        }
    }


    @Scheduled(cron = "${scheduler.cron_srte}", zone = "${scheduler.zone}")
    public void scheduleSRTEDataFetch() throws DataPollException {
        if (srtePollEnabled) {
            logger.info("CRON STARTED FOR POLLING SRTE");
            logger.info("{}, {} FOR SRTE", cron, zone);
            srteDataHandlingService.handlingExchangedData();
            closePoller();
        }

    }

    private void closePoller() {
        if (singlePoll) {
            System.exit(0);
        }
    }
}
