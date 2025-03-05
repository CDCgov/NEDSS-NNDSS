package gov.cdc.nnddatapollservice.universal.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.universal.dao.EdxNbsOdseDataPersistentDAO;
import gov.cdc.nnddatapollservice.universal.dao.UniversalDataPersistentDAO;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import gov.cdc.nnddatapollservice.universal.service.interfaces.IUniversalDataHandlingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;
import static gov.cdc.nnddatapollservice.share.TimestampUtil.getCurrentTimestamp;

@Service
@Slf4j
public class UniversalDataHandlingService implements IUniversalDataHandlingService {
    private static Logger logger = LoggerFactory.getLogger(UniversalDataHandlingService.class);
    private static final int THREAD_POOL_SIZE = 5; // Adjust based on system capacity
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static final int THREAD_CHECK = 10000;

    @Value("${datasync.store_in_local}")
    protected boolean storeJsonInLocalFolder = false;
    @Value("${datasync.store_in_S3}")
    protected boolean storeJsonInS3 = false;
    @Value("${datasync.store_in_sql}")
    protected boolean storeInSql = false;
    @Value("${datasync.data_sync_batch_limit}")
    protected Integer pullLimit = 0;
    @Value("${datasync.data_sync_delete_on_initial}")
    protected boolean deleteOnInit = false;

    private final UniversalDataPersistentDAO universalDataPersistentDAO;
    private final IPollCommonService iPollCommonService;
    private final IS3DataService is3DataService;
    private final EdxNbsOdseDataPersistentDAO edxNbsOdseDataPersistentDAO;

    public UniversalDataHandlingService(UniversalDataPersistentDAO universalDataPersistentDAO,
                                        IPollCommonService iPollCommonService,
                                        IS3DataService is3DataService,
                                        EdxNbsOdseDataPersistentDAO edxNbsOdseDataPersistentDAO) {
        this.universalDataPersistentDAO = universalDataPersistentDAO;
        this.iPollCommonService = iPollCommonService;
        this.is3DataService = is3DataService;
        this.edxNbsOdseDataPersistentDAO = edxNbsOdseDataPersistentDAO;
    }

    public void handlingExchangedData(String source) {
        List<PollDataSyncConfig> configTableList = iPollCommonService.getTableListFromConfig();
        List<PollDataSyncConfig> filteredTablesList = iPollCommonService.getTablesConfigListBySOurceDB(configTableList, source);

        boolean isInitialLoad = iPollCommonService.checkPollingIsInitailLoad(filteredTablesList);

        if (isInitialLoad && storeInSql && deleteOnInit) {
            cleanupTables(filteredTablesList);
        }

        List<PollDataSyncConfig> descList = filteredTablesList.stream()
                .sorted((a, b) -> Integer.compare(b.getTableOrder(), a.getTableOrder())) // Sorting in descending order
                .toList();

        for(PollDataSyncConfig pollDataSyncConfig : descList) {
            if (pollDataSyncConfig.isRecreateApplied() && storeInSql) {
                iPollCommonService.deleteTable(pollDataSyncConfig.getTableName());
            }
        }

        List<PollDataSyncConfig> ascList = filteredTablesList.stream()
                .sorted((a, b) -> Integer.compare(a.getTableOrder(), b.getTableOrder())) // Sort by tableOrder ASC
                .toList();


        for (PollDataSyncConfig pollDataSyncConfig : ascList) {
            pollAndPersistData(isInitialLoad, pollDataSyncConfig);
        }

    }

    @SuppressWarnings({"java:S1141","java:S3776"})
    protected void pollAndPersistData(boolean isInitialLoad, PollDataSyncConfig config)  {
        try {
            LogResponseModel log = null;
            boolean exceptionAtApiLevel = false;
            Integer totalRecordCounts = 0;

            if(config.isRecreateApplied() ) {
                // IF recreated applied, EXPLICITLY set initialLoad to true, so the flow can be rerun
                isInitialLoad = true;
            }
            String timeStampForPoll = getPollTimestamp(isInitialLoad, config.getTableName());
            var timestampWithNull = getCurrentTimestamp();
            var startTime = getCurrentTimestamp();

            String logStr = "";


            try {
                totalRecordCounts = iPollCommonService.callDataCountEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll);
            } catch (Exception e) {
                log = new LogResponseModel(CRITICAL_COUNT_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                iPollCommonService.updateLogNoTimestamp(config.getTableName(), log);
                throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
            }



            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double) totalRecordCounts / batchSize);

            if (!config.isNoPagination()) {
                try {
                    var encodedDataWithNull = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, true,
                            "0", "0", false);
                    var rawJsonDataWithNull = iPollCommonService.decodeAndDecompress(encodedDataWithNull);
                    if (storeJsonInS3) {
                        log = is3DataService.persistToS3MultiPart(config.getSourceDb(), rawJsonDataWithNull, config.getTableName(), timestampWithNull, isInitialLoad);
                        log.setStartTime(startTime);
                        log.setLog(S3_LOG + (log.getLog() == null || log.getLog().isEmpty()? SUCCESS : log.getLog()));
                        log.setStatus(log.getStatus().equalsIgnoreCase(ERROR)? log.getStatus() : SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestampWithNull, log);
                    }
                    else if (storeInSql) {
                        if (config.getSourceDb().equalsIgnoreCase(NBS_ODSE_EDX)) {
                            log =  edxNbsOdseDataPersistentDAO.saveNbsOdseData(config.getTableName(), rawJsonDataWithNull);
                        }
                        else {
                            log =  universalDataPersistentDAO.saveRdbModernData(config, rawJsonDataWithNull, isInitialLoad);
                        }
                        log.setStartTime(startTime);
                        log.setLog(SQL_LOG + (log.getLog() == null ||log.getLog().isEmpty()? SUCCESS : log.getLog()));
                        log.setStatus(log.getStatus().equalsIgnoreCase(ERROR)? log.getStatus() : SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestampWithNull, log);
                    }
                    else  {
                        log = iPollCommonService.writeJsonDataToFile(config.getSourceDb(), config.getTableName(), timestampWithNull, rawJsonDataWithNull);
                        log.setStartTime(startTime);
                        log.setLog(LOCAL_DIR_LOG + (log.getLog() == null ||log.getLog().isEmpty()? SUCCESS : log.getLog()));
                        log.setStatus(log.getStatus().equalsIgnoreCase(ERROR)? log.getStatus() : SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestampWithNull, log);
                    }
                }
                catch (Exception e) {
                    log = new LogResponseModel(CRITICAL_NULL_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime);
                    iPollCommonService.updateLogNoTimestamp(config.getTableName(), log);
                    throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
                }

//                if (totalRecordCounts >= THREAD_CHECK) {
//                    processingDataBatchMultiThreadSemaphore( totalPages,  batchSize,  isInitialLoad,  timeStampForPoll,
//                            config,  logStr,  exceptionAtApiLevel, startTime);
//                } else {
//                    processingDataBatch( totalPages,  batchSize,  isInitialLoad,  timeStampForPoll,
//                         config,  logStr,  exceptionAtApiLevel, startTime);
//                }

                processingDataBatchMultiThreadSemaphore( totalPages,  batchSize,  isInitialLoad,  timeStampForPoll,
                        config,  logStr,  exceptionAtApiLevel, startTime);


            }
            else
            {
                String rawJsonData = "";
                Timestamp timestamp = null;

                try {
                    String encodedData = "";
                    encodedData = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, false,
                            "0", "0", true);


                    rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                    timestamp = getCurrentTimestamp();
                } catch (Exception e) {
                    logStr = e.getMessage();
                    exceptionAtApiLevel = true;
                }

                updateDataHelper(exceptionAtApiLevel, timestamp,
                        rawJsonData, isInitialLoad, logStr,
                        startTime, config);
            }




        } catch (Exception e) {
            logger.error("TASK failed. tableName: {}, message: {}", config.getTableName(), getStackTraceAsString(e));
        }
    }


    protected void processingDataBatchMultiThreadSemaphore(int totalPages, int batchSize, boolean isInitialLoad, String timeStampForPoll,
                                                           PollDataSyncConfig config, String logStr, boolean exceptionAtApiLevel,
                                                           Timestamp startTime) {
        // Assuming SLF4J logging, adjust as needed
        Logger logger = LoggerFactory.getLogger(getClass());

        int batchSizeForProcessing = 10_000; // Process 10,000 pages per batch
        int initialConcurrency = 100; // Initial concurrent API calls, adjust based on API limit
        int maxConcurrency = 200; // Hard cap to prevent API overload (e.g., 200 req/sec)

        logger.info("Starting processing of {} pages in batches of {} with initial concurrency {}",
                totalPages, batchSizeForProcessing, initialConcurrency);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Process in batches
            for (int start = 0; start < totalPages; start += batchSizeForProcessing) {
                int end = Math.min(start + batchSizeForProcessing, totalPages);
                List<Future<?>> futures = new ArrayList<>(end - start); // Pre-size for current batch
                Semaphore semaphore = new Semaphore(initialConcurrency);
                AtomicInteger currentConcurrency = new AtomicInteger(initialConcurrency);

                // Metrics for dynamic adjustment within batch
                AtomicInteger errorCount = new AtomicInteger(0);
                AtomicLong totalResponseTime = new AtomicLong(0);
                AtomicInteger taskCount = new AtomicInteger(0);

                logger.info("Processing batch: pages {} to {}", start, end - 1);

                // Submit tasks for current batch
                for (int i = start; i < end; i++) {
                    final int pageIndex = i;
                    Future<?> future = executor.submit(() -> {
                        boolean permitAcquired = false;
                        try {
                            semaphore.acquire();
                            permitAcquired = true;
                            long startTimeMs = System.currentTimeMillis();
                            String rawJsonData = "";
                            Timestamp timestamp = null;
                            String localLogStr = logStr;
                            boolean localExceptionAtApiLevel = exceptionAtApiLevel;

                            try {
                                int startRow = pageIndex * batchSize + 1;
                                int endRow = (pageIndex + 1) * batchSize;

                                logger.debug("Processing page {} (rows {}-{})", pageIndex, startRow, endRow);

                                String encodedData = iPollCommonService.callDataExchangeEndpoint(
                                        config.getTableName(),
                                        isInitialLoad,
                                        timeStampForPoll,
                                        false,
                                        String.valueOf(startRow),
                                        String.valueOf(endRow),
                                        false
                                );

                                rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                                timestamp = getCurrentTimestamp();
                            } catch (Exception e) {
                                localLogStr = e.getMessage();
                                localExceptionAtApiLevel = true;
                                errorCount.incrementAndGet();
                                logger.error("Error processing page {}: {}", pageIndex, e.getMessage(), e);
                            } finally {
                                long responseTime = System.currentTimeMillis() - startTimeMs;
                                totalResponseTime.addAndGet(responseTime);
                                int completedTasks = taskCount.incrementAndGet();

                                if (completedTasks % 100 == 0) {
                                    adjustConcurrency(semaphore, currentConcurrency, errorCount, totalResponseTime, completedTasks, maxConcurrency);
                                }
                                semaphore.release();
                                permitAcquired = false;
                            }

                            updateDataHelper(
                                    localExceptionAtApiLevel,
                                    timestamp,
                                    rawJsonData,
                                    isInitialLoad,
                                    localLogStr,
                                    startTime,
                                    config
                            );
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            logger.warn("Task for page {} interrupted", pageIndex);
                        } finally {
                            if (permitAcquired) {
                                semaphore.release(); // Ensure release on interruption
                            }
                        }
                    });
                    futures.add(future);
                }

                // Wait for batch to complete with timeout
                long timeoutPerTaskMs = 30_000; // 30 seconds per task
                int completedTasks = 0;
                for (Future<?> future : futures) {
                    try {
                        future.get(timeoutPerTaskMs, TimeUnit.MILLISECONDS);
                        completedTasks++;
                    } catch (TimeoutException e) {
                        int pageIndex = start + futures.indexOf(future);
                        logger.error("Task for page {} timed out after {} ms", pageIndex, timeoutPerTaskMs);
                        future.cancel(true);
                    } catch (InterruptedException e) {
                        logger.warn("Interrupted while waiting for batch completion");
                        Thread.currentThread().interrupt();
                        break;
                    } catch (ExecutionException e) {
                        int pageIndex = start + futures.indexOf(future);
                        logger.error("Task for page {} failed: {}", pageIndex, e.getCause().getMessage(), e.getCause());
                    }
                }

                logger.info("Batch completed: {}/{} pages processed (total so far: {})",
                        completedTasks, end - start, start + completedTasks);
                if (completedTasks < end - start) {
                    logger.warn("Batch processed fewer pages than expected: {} < {}", completedTasks, end - start);
                }
            }

            // Final executor shutdown
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.warn("Executor did not terminate cleanly, forcing shutdown");
                executor.shutdownNow();
            }

        } catch (Exception e) {
            logger.error("Unexpected error in processing batch: {}", e.getMessage(), e);
        }
    }

    // Adjusted concurrency method with max limit
    private void adjustConcurrency(Semaphore semaphore, AtomicInteger currentConcurrency,
                                   AtomicInteger errorCount, AtomicLong totalResponseTime, int taskCount, int maxConcurrency) {
        double avgResponseTime = (double) totalResponseTime.get() / taskCount;
        double errorRate = (double) errorCount.get() / taskCount;

        int currentPermits = currentConcurrency.get();
        int newPermits = currentPermits;

        // Adjust based on response time and error rate
        if (errorRate > 0.1) { // Error rate > 10%, reduce concurrency
            newPermits = Math.max(10, currentPermits - 10); // Minimum 10
        } else if (avgResponseTime < 50) { // Response < 50ms, increase concurrency
            newPermits = Math.min(maxConcurrency, currentPermits + 10); // Cap at maxConcurrency
        } else if (avgResponseTime > 200) { // Response > 200ms, decrease concurrency
            newPermits = Math.max(10, currentPermits - 10);
        }

        if (newPermits != currentPermits) {
            int delta = newPermits - currentPermits;
            if (delta > 0) {
                semaphore.release(delta);
                currentConcurrency.set(newPermits);
                logger.info("Increased concurrency to: {} (avgResponseTime: {}ms, errorRate: {})", newPermits, avgResponseTime, errorRate);
            } else if (delta < 0) {
                currentConcurrency.set(newPermits);
                logger.info("Desired concurrency reduced to: {}, waiting for tasks to complete (avgResponseTime: {}ms, errorRate: {})",
                        newPermits, avgResponseTime, errorRate);
            }
        }
    }


    protected void processingDataBatch(int totalPages, int batchSize, boolean isInitialLoad, String timeStampForPoll,
                                       PollDataSyncConfig config, String logStr, boolean exceptionAtApiLevel,
                                       Timestamp startTime) {
        for (int i = 0; i < totalPages; i++) {
            String rawJsonData = "";
            Timestamp timestamp = null;
            try {
                int startRow = i * batchSize + 1;
                int endRow = (i + 1) * batchSize;

                String encodedData = "";
                encodedData = iPollCommonService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, false,
                        String.valueOf(startRow), String.valueOf(endRow), false);

                rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                timestamp = getCurrentTimestamp();
            } catch (Exception e) {
                logStr = e.getMessage();
                exceptionAtApiLevel = true;
            }

            updateDataHelper(exceptionAtApiLevel, timestamp,
                    rawJsonData, isInitialLoad, logStr,
                    startTime, config);

        }
    }

    protected String getPollTimestamp(boolean isInitialLoad, String tableName) {
        String timeStampForPoll;
        if (isInitialLoad) {
            timeStampForPoll = iPollCommonService.getCurrentTimestamp();
        } else {
            if(storeJsonInS3) {
                timeStampForPoll = iPollCommonService.getLastUpdatedTimeS3(tableName);
            }
            else if (storeInSql)
            {
                timeStampForPoll = iPollCommonService.getLastUpdatedTime(tableName);
            }
            else
            {
                timeStampForPoll = iPollCommonService.getLastUpdatedTimeLocalDir(tableName);
            }
        }
        return timeStampForPoll;
    }

    @SuppressWarnings("java:S107")
    protected void updateDataHelper(boolean exceptionAtApiLevel, Timestamp timestamp,
                                    String rawJsonData, boolean isInitialLoad, String log,
                                    Timestamp startTime, PollDataSyncConfig config) {
        LogResponseModel logResponseModel = new LogResponseModel();

        try {
            if (exceptionAtApiLevel) {
                logResponseModel.setStatus(ERROR);
                logResponseModel.setStartTime(startTime);
                logResponseModel.setLog(API_LEVEL + log);

                if (storeInSql) {
                    iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestamp, logResponseModel);
                }
                else if (storeJsonInS3)
                {
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestamp, logResponseModel);
                }
                else
                {
                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestamp, logResponseModel);
                }
            } else {
                if (storeJsonInS3) {
                    logResponseModel = is3DataService.persistToS3MultiPart(config.getSourceDb(), rawJsonData, config.getTableName(), timestamp, isInitialLoad);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(S3_LOG + (log == null || log.isEmpty()? SUCCESS : log));
                    logResponseModel.setStatus(logResponseModel.getStatus().equalsIgnoreCase(ERROR)? logResponseModel.getStatus() : SUCCESS);
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestamp, logResponseModel);
                }
                else if (storeInSql)
                {
                    if (config.getSourceDb().equalsIgnoreCase(NBS_ODSE_EDX)) {
                        logResponseModel = edxNbsOdseDataPersistentDAO.saveNbsOdseData(config.getTableName(), rawJsonData);
                    }
                    else {
                        logResponseModel = universalDataPersistentDAO.saveRdbModernData(config, rawJsonData, isInitialLoad);

                    }
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(SQL_LOG + (log == null || log.isEmpty()? SUCCESS : log));
                    logResponseModel.setStatus(logResponseModel.getStatus().equalsIgnoreCase(ERROR)? logResponseModel.getStatus() : SUCCESS);
                    iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestamp, logResponseModel);
                }
                else
                {
                    logResponseModel = iPollCommonService.writeJsonDataToFile(config.getSourceDb(), config.getTableName(), timestamp, rawJsonData);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(LOCAL_DIR_LOG + (log == null || log.isEmpty()? SUCCESS : log));
                    logResponseModel.setStatus(logResponseModel.getStatus().equalsIgnoreCase(ERROR)? logResponseModel.getStatus() : SUCCESS);

                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestamp, logResponseModel);
                }
            }
        } catch (Exception e) {
            logResponseModel = new LogResponseModel();
            logResponseModel.setStatus(ERROR);
            logResponseModel.setStartTime(startTime);
            logResponseModel.setLog(CRITICAL_NON_NULL_LOG + e.getMessage());
            logResponseModel.setStackTrace(getStackTraceAsString(e));
            iPollCommonService.updateLogNoTimestamp(config.getTableName(), logResponseModel);
        }
    }


    private void cleanupTables(List<PollDataSyncConfig> configTableList) {
        for (int j = configTableList.size() - 1; j >= 0; j = j - 1) {
            iPollCommonService.deleteTable(configTableList.get(j).getTableName());
        }
    }
}