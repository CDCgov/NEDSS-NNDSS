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
import java.util.Arrays;
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

    protected int tableLevelMaxConcurrentThreads = 5;  // Limit to 2 threads running simultaneously
    protected long tableLevelTimeoutPerTaskMs = 120_000; // 2 minutes per task


    protected int apiLevelBatchSizeForProcessing = 3; // Process 3 pages (30,000 records) per batch
    protected int apiLevelInitialConcurrency = 10;    // Start with 10 concurrent tasks
    protected int apiLevelMaxConcurrency = 50;       // Cap at 50 (half of Hikari pool size for safety)
    protected int apiLevelMaxRetries = 5;            // Retry up to 3 times
    protected long apiLevelTimeoutPerTaskMs = 120_000; // 2 minutes per task for large batches

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

        int maxConcurrentThreads = tableLevelMaxConcurrentThreads;
        long timeoutPerTaskMs = tableLevelTimeoutPerTaskMs;

        // Split the list into threaded and non-threaded entries
        List<PollDataSyncConfig> threadedConfigs = new ArrayList<>();
        List<PollDataSyncConfig> sequentialConfigs = new ArrayList<>();
        for (PollDataSyncConfig config : ascList) {
            if (config.getTableOrder() == 1) {
                threadedConfigs.add(config);
            } else {
                sequentialConfigs.add(config);
            }
        }
//
//        for (PollDataSyncConfig pollDataSyncConfig : ascList) {
//            pollAndPersistData(isInitialLoad, pollDataSyncConfig);
//        }

        logger.info("Processing {} PollDataSyncConfig entries: {} threaded (TableOder=1) with max 2 concurrent threads, {} sequential (TableOder!=1)",
                ascList.size(), threadedConfigs.size(), sequentialConfigs.size());

        // Process threaded entries (TableOder == 1) using virtual threads with a max of 2 concurrent threads
        if (!threadedConfigs.isEmpty()) {
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<Future<?>> futures = new ArrayList<>(threadedConfigs.size());
                Semaphore semaphore = new Semaphore(maxConcurrentThreads); // Fixed at 2

                // Submit tasks for threaded entries
                for (int i = 0; i < threadedConfigs.size(); i++) {
                    final int index = i;
                    final PollDataSyncConfig pollDataSyncConfig = threadedConfigs.get(i);
                    Future<?> future = executor.submit(() -> {
                        boolean permitAcquired = false;
                        try {
                            semaphore.acquire();
                            permitAcquired = true;

                            logger.debug("Processing PollDataSyncConfig (threaded) at index {}: {}", index, pollDataSyncConfig.getTableName());
                            pollAndPersistData(isInitialLoad, pollDataSyncConfig);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            logger.warn("Task for PollDataSyncConfig (threaded) at index {} interrupted", index);
                        } catch (Exception e) {
                            logger.error("Error processing PollDataSyncConfig (threaded) at index {}: {}", index, e.getMessage(), e);
                        } finally {
                            if (permitAcquired) {
                                semaphore.release();
                            }
                        }
                    });
                    futures.add(future);
                }

                // Wait for threaded tasks to complete
                int completedTasks = 0;
                for (int i = 0; i < futures.size(); i++) {
                    Future<?> future = futures.get(i);
                    int index = i;
                    try {
                        future.get(timeoutPerTaskMs, TimeUnit.MILLISECONDS);
                        completedTasks++;
                    } catch (TimeoutException e) {
                        logger.error("Task for PollDataSyncConfig (threaded) at index {} timed out after {} ms", index, timeoutPerTaskMs);
                        future.cancel(true); // Cancel to free resources
                    } catch (InterruptedException e) {
                        logger.warn("Interrupted while waiting for threaded task completion");
                        Thread.currentThread().interrupt();
                        break;
                    } catch (ExecutionException e) {
                        logger.error("Task for PollDataSyncConfig (threaded) at index {} failed: {}", index, e.getCause().getMessage(), e.getCause());
                    }
                }

                logger.info("Threaded processing completed: {}/{} PollDataSyncConfig entries processed", completedTasks, threadedConfigs.size());
                if (completedTasks < threadedConfigs.size()) {
                    logger.warn("Processed fewer threaded entries than expected: {} < {}", completedTasks, threadedConfigs.size());
                }

                // Clean shutdown
                executor.shutdown();
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    logger.warn("Executor did not terminate cleanly, forcing shutdown");
                    executor.shutdownNow();
                }
            } catch (Exception e) {
                logger.error("Unexpected error in processing threaded PollDataSyncConfig entries: {}", e.getMessage(), e);
            }
        }

        for (PollDataSyncConfig pollDataSyncConfig : sequentialConfigs) {
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
                            log =  universalDataPersistentDAO.saveUniversalData(config, rawJsonDataWithNull, isInitialLoad, startTime);
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


                if (totalRecordCounts >= THREAD_CHECK && !SPECIAL_TABLES.contains(config.getTableName().toUpperCase())) {
                    processingDataBatchMultiThreadSemaphore( totalPages,  batchSize,  isInitialLoad,  timeStampForPoll,
                            config,  logStr,  exceptionAtApiLevel, startTime, totalRecordCounts);
                } else {
                    processingDataBatch( totalPages,  batchSize,  isInitialLoad,  timeStampForPoll,
                         config,  logStr,  exceptionAtApiLevel, startTime);
                }

//                processingDataBatchMultiThreadSemaphore( totalPages,  batchSize,  isInitialLoad,  timeStampForPoll,
//                        config,  logStr,  exceptionAtApiLevel, startTime);


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
                                                           Timestamp startTime, int totalRecordCounts) {
        int batchSizeForProcessing = apiLevelBatchSizeForProcessing; // Process 3 pages (30,000 records) per batch
        int initialConcurrency = apiLevelInitialConcurrency;    // Start with 10 concurrent tasks
        int maxConcurrency = apiLevelMaxConcurrency;       // Cap at 50 (half of Hikari pool size for safety)
        int maxRetries = apiLevelMaxRetries;            // Retry up to 3 times
        long timeoutPerTaskMs = apiLevelTimeoutPerTaskMs; // 2 minutes per task for large batches

        logger.info("Starting processing of {} pages (batchSize={}) in batches of {} with concurrency {}-{}",
                totalPages, batchSize, batchSizeForProcessing, initialConcurrency, maxConcurrency);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int start = 0; start < totalPages; start += batchSizeForProcessing) {
                int end = Math.min(start + batchSizeForProcessing, totalPages);
                List<Future<?>> futures = new ArrayList<>(end - start);
                Semaphore semaphore = new Semaphore(initialConcurrency);
                AtomicInteger currentConcurrency = new AtomicInteger(initialConcurrency);
                AtomicInteger errorCount = new AtomicInteger(0);
                AtomicLong totalResponseTime = new AtomicLong(0);
                AtomicInteger taskCount = new AtomicInteger(0);

                logger.info("Processing batch: pages {} to {} ({} pages)", start, end - 1, end - start);

                // Submit tasks for current batch
                for (int i = start; i < end; i++) {
                    final int pageIndex = i;
                    Future<?> future = executor.submit(() -> {
                        boolean permitAcquired = false;
                        int retries = 0;
                        while (retries < maxRetries) {
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
                                    int endRow = Math.min((pageIndex + 1) * batchSize, totalRecordCounts ); // Respect total records
                                    logger.debug("Processing page {} (rows {}-{})", pageIndex, startRow, endRow);

                                    String encodedData = iPollCommonService.callDataExchangeEndpoint(
                                            config.getTableName(), isInitialLoad, timeStampForPoll, false,
                                            String.valueOf(startRow), String.valueOf(endRow), false
                                    );
                                    rawJsonData = iPollCommonService.decodeAndDecompress(encodedData);
                                    timestamp = getCurrentTimestamp();

                                    updateDataHelper(localExceptionAtApiLevel, timestamp, rawJsonData,
                                            isInitialLoad, localLogStr, startTime, config);
                                    break; // Success, exit retry loop
                                } catch (Exception e) {
                                    localLogStr = e.getMessage();
                                    localExceptionAtApiLevel = true;
                                    errorCount.incrementAndGet();
                                    logger.error("Error on page {} (attempt {}/{}): {}", pageIndex, retries + 1, maxRetries, e.getMessage(), e);
                                    if (retries == maxRetries - 1) {
                                        throw e; // Final failure
                                    }
                                    Thread.sleep(2000 * (retries + 1)); // Backoff: 2s, 4s, 6s
                                } finally {
                                    long responseTime = System.currentTimeMillis() - startTimeMs;
                                    totalResponseTime.addAndGet(responseTime);
                                    int completedTasks = taskCount.incrementAndGet();
                                    if (completedTasks % 3 == 0) { // Adjust more frequently for small batches
                                        adjustConcurrency(semaphore, currentConcurrency, errorCount, totalResponseTime, completedTasks, maxConcurrency);
                                    }
                                    if (permitAcquired) {
                                        semaphore.release();
                                        permitAcquired = false;
                                    }
                                }
                            } catch (InterruptedException | DataPollException e) {
                                Thread.currentThread().interrupt();
                                logger.warn("Task for page {} interrupted", pageIndex);
                                break; // Exit on interruption
                            }
                        }
                    });
                    futures.add(future);
                }

                List<Integer> failedPages = new ArrayList<>(); // Track failed pages for reprocessing
                // Wait for batch to complete
                int completedTasks = 0;
                for (int i = 0; i < futures.size(); i++) {
                    Future<?> future = futures.get(i);
                    int pageIndex = start + i;
                    try {
                        future.get(timeoutPerTaskMs, TimeUnit.MILLISECONDS);
                        completedTasks++;
                    } catch (TimeoutException e) {
                        logger.error("Task for page {} timed out after {} ms", pageIndex, timeoutPerTaskMs);
                        future.cancel(true); // Cancel to free resources
                        failedPages.add(pageIndex); // Mark for reprocessing
                    } catch (InterruptedException e) {
                        logger.warn("Interrupted while waiting for batch completion");
                        Thread.currentThread().interrupt();
                        failedPages.add(pageIndex); // Mark for reprocessing
                        break;
                    } catch (ExecutionException e) {
                        logger.error("Task for page {} failed: {}", pageIndex, e.getCause().getMessage(), e.getCause());
                        failedPages.add(pageIndex); // Mark for reprocessing
                    }
                }

                logger.info("Batch completed: {}/{} pages processed (total so far: {})",
                        completedTasks, end - start, start + completedTasks);
                if (completedTasks < end - start) {
                    logger.warn("Batch processed fewer pages than expected: {} < {}", completedTasks, end - start);
                }

                if (!failedPages.isEmpty()) {
                    logger.info("Check {} failed pages sequentially: {}", failedPages.size(), failedPages);
                }
            }

            // Clean shutdown
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                logger.warn("Executor did not terminate cleanly, forcing shutdown");
                executor.shutdownNow();
            }
        } catch (Exception e) {
            logger.error("Unexpected error in processing batch: {}", e.getMessage(), e);
        }
    }

    // Adjusted concurrency method with max limit
    private void adjustConcurrency(Semaphore semaphore, AtomicInteger currentConcurrency,
                                   AtomicInteger errorCount, AtomicLong totalResponseTime,
                                   int taskCount, int maxConcurrency) {
        double avgResponseTime = taskCount > 0 ? (double) totalResponseTime.get() / taskCount : 0;
        double errorRate = taskCount > 0 ? (double) errorCount.get() / taskCount : 0;

        int currentPermits = currentConcurrency.get();
        int newPermits = currentPermits;

        if (errorRate > 0.2) { // >20% error rate, reduce concurrency
            newPermits = Math.max(5, currentPermits - 5); // Minimum 5
        } else if (avgResponseTime < 100 && errorRate < 0.05) { // <100ms, <5% errors, increase
            newPermits = Math.min(maxConcurrency, currentPermits + 5);
        } else if (avgResponseTime > 500) { // >500ms, decrease
            newPermits = Math.max(5, currentPermits - 5);
        }

        if (newPermits != currentPermits) {
            int delta = newPermits - currentPermits;
            if (delta > 0) {
                semaphore.release(delta);
                currentConcurrency.set(newPermits);
                logger.info("Increased concurrency to: {} (avgResponseTime: {}ms, errorRate: {})", newPermits, avgResponseTime, errorRate);
            } else if (delta < 0) {
                currentConcurrency.set(newPermits);
                logger.info("Reduced concurrency to: {} (avgResponseTime: {}ms, errorRate: {})", newPermits, avgResponseTime, errorRate);
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
                        logResponseModel = universalDataPersistentDAO.saveUniversalData(config, rawJsonData, isInitialLoad, startTime);

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