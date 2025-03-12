package gov.cdc.nnddatapollservice.service;

import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.IDataPullService;
import gov.cdc.nnddatapollservice.service.interfaces.INNDDataHandlingService;
import gov.cdc.nnddatapollservice.universal.service.interfaces.IUniversalDataHandlingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;

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

    @Value("${poll.odse.enabled}")
    private boolean odsePollEnabled;

    @Value("${poll.covid_datamart.enabled}")
    private boolean covidDataMartEnabled;
    @Value("${poll.srte.enabled}")
    private boolean srtePollEnabled;

    @Value("${poll.edx_activity.enabled}")
    private boolean edxActivityEnabled;

    @Value("${poll.single_time_poll_enabled}")
    private boolean singlePoll;

    private final INNDDataHandlingService dataHandlingService;
    private final IUniversalDataHandlingService universalDataHandlingService;

    public DataPullService(INNDDataHandlingService dataHandlingService,
                           IUniversalDataHandlingService universalDataHandlingService) {
        this.dataHandlingService = dataHandlingService;
        this.universalDataHandlingService = universalDataHandlingService;
    }

    @Scheduled(cron = "${scheduler.cron}", zone = "${scheduler.zone}")
    public void scheduleNNDDataFetch() throws DataPollException {
        if (nndPollEnabled) {
            logger.info("START POLLING");
            logger.info("CRON: {}, TZ: {}", cron, zone);
            dataHandlingService.handlingExchangedData();
            logger.info("END POLLING");
            closePoller();
        }
    }
    @Scheduled(cron = "${scheduler.cron-data-sync}", zone = "${scheduler.zone}")
    public void scheduleDataSync() {
        logger.info("CRON: {}, TZ: {}", cron, zone);
        try {
            if (rdbPollEnabled) {
                logger.info("START RDB POLLING");
                universalDataHandlingService.handlingExchangedData(RDB);
                logger.info("END RDB POLLING");
            }
            if (edxActivityEnabled) {
                logger.info("START EDX POLLING");
                universalDataHandlingService.handlingExchangedData(NBS_ODSE_EDX);
                logger.info("END EDX POLLING");
            }
            if (rdbModernPollEnabled) {
                logger.info("START RDB MOD POLLING");
                universalDataHandlingService.handlingExchangedData(RDB_MODERN);
                logger.info("END RDB MOD POLLING");
            }
            if (covidDataMartEnabled) {
                logger.info("START COVID POLLING");
                universalDataHandlingService.handlingExchangedData(COVID_DATAMART);
                logger.info("END COVID POLLING");
            }
            if (odsePollEnabled) {
                logger.info("START ODSE POLLING");
                universalDataHandlingService.handlingExchangedData(ODSE_OBS);
                logger.info("END ODSE POLLING");
            }
            if (srtePollEnabled) {
                logger.info("START SRTE POLLING");
                universalDataHandlingService.handlingExchangedData(SRTE);
                logger.info("END SRTE POLLING");
            }
            closePoller();
        } catch (Exception e) {
            logger.error("Exception in main Cron job. Shutting down...");
            logger.error(e.getMessage(), e);
            closePoller();
        }


    }


    private void closePoller() {
        if (singlePoll) {
            System.exit(0);
        }
    }
}
