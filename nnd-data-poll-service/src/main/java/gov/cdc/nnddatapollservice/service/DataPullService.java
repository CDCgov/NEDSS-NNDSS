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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DataPullService implements IDataPullService {

    private static final Logger logger = LoggerFactory.getLogger(DataPullService.class);

    @Value("${scheduler.nnd.cron}")
    private String nndCron;
    @Value("${scheduler.rdb.cron}")
    private String rdbCron;
    @Value("${scheduler.rdb_modern.cron}")
    private String rdbModernCron;
    @Value("${scheduler.srte.cron}")
    private String srteCron;
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

    private final INNDDataHandlingService dataHandlingService;
    private final IRdbDataHandlingService rdbDataHandlingService;
    private final IRdbModernDataHandlingService rdbModernDataHandlingService;
    private final ISrteDataHandlingService srteDataHandlingService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private CountDownLatch latch; // Added to track remaining active jobs

    public DataPullService(INNDDataHandlingService dataHandlingService,
                           IRdbDataHandlingService rdbDataHandlingService,
                           IRdbModernDataHandlingService rdbModernDataHandlingService,
                           ISrteDataHandlingService srteDataHandlingService) {
        this.dataHandlingService = dataHandlingService;
        this.rdbDataHandlingService = rdbDataHandlingService;
        this.rdbModernDataHandlingService = rdbModernDataHandlingService;
        this.srteDataHandlingService = srteDataHandlingService;
    }

    @Scheduled(cron = "${scheduler.nnd.cron}", zone = "${scheduler.zone}")
    public void scheduleNNDDataFetch() {
        if (nndPollEnabled) {
            runTaskInThread("NND", () -> {
                try {
                    dataHandlingService.handlingExchangedData();
                } catch (DataPollException e) {
                    logger.error("Error during NND data handling: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Scheduled(cron = "${scheduler.rdb.cron}", zone = "${scheduler.zone}")
    public void scheduleRDBDataFetch() {
        if (rdbPollEnabled) {
            runTaskInThread("RDB", () -> {
                try {
                    rdbDataHandlingService.handlingExchangedData();
                } catch (DataPollException e) {
                    logger.error("Error during RDB data handling: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Scheduled(cron = "${scheduler.rdb_modern.cron}", zone = "${scheduler.zone}")
    public void scheduleRdbModernDataFetch() {
        if (rdbModernPollEnabled) {
            runTaskInThread("RDB_MODERN", () -> {
                try {
                    rdbModernDataHandlingService.handlingExchangedData();
                } catch (DataPollException e) {
                    logger.error("Error during RDB_MODERN data handling: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Scheduled(cron = "${scheduler.srte.cron}", zone = "${scheduler.zone}")
    public void scheduleSRTEDataFetch() {
        if (srtePollEnabled) {
            runTaskInThread("SRTE", () -> {
                try {
                    srteDataHandlingService.handlingExchangedData();
                } catch (DataPollException e) {
                    logger.error("Error during SRTE data handling: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private synchronized void runTaskInThread(String taskName, Runnable task) {
        // Initialize the latch if it's the first task
        if (latch == null) {
            int activePolls = getActivePollCount();
            latch = new CountDownLatch(activePolls);
        }

        executorService.submit(() -> {
            try {
                logger.info("Starting {} data poll", taskName);
                task.run();
                logger.info("{} data poll completed", taskName);
            } catch (RuntimeException e) {
                logger.error("Error occurred during {} data poll: {}", taskName, e.getMessage());
            } finally {
                latch.countDown(); // Decrease the latch count when a task is finished
                if (singlePoll && latch.getCount() == 0) {
                    shutdownPoller();
                }
            }
        });
    }

    private int getActivePollCount() {
        int count = 0;
        if (nndPollEnabled) count++;
        if (rdbPollEnabled) count++;
        if (rdbModernPollEnabled) count++;
        if (srtePollEnabled) count++;
        return count;
    }

    private void shutdownPoller() {
        logger.info("Shutting down after single poll");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("Executor did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
