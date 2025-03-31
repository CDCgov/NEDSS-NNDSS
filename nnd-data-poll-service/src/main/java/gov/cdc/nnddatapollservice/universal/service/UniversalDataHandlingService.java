package gov.cdc.nnddatapollservice.universal.service;

import gov.cdc.nnddatapollservice.exception.APIException;
import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.interfaces.IApiService;
import gov.cdc.nnddatapollservice.service.interfaces.IPollCommonService;
import gov.cdc.nnddatapollservice.service.interfaces.IS3DataService;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
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
import static gov.cdc.nnddatapollservice.share.StringUtil.hasOnlyOneKey;
import static gov.cdc.nnddatapollservice.share.TimestampUtil.getCurrentTimestamp;

@Service
@Slf4j
public class UniversalDataHandlingService implements IUniversalDataHandlingService {
    private static final Logger logger = LoggerFactory.getLogger(UniversalDataHandlingService.class);
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


    // These are table level task - probably dont need this
    @Value("${thread.table-level.enabled}")
    protected boolean multiThreadTableLevelEnabled = false;
    @Value("${thread.table-level.max-concurrency}")
    protected int tableLevelMaxConcurrentThreads = 1;
//    @Value("${thread.table-level.timeout}")
    protected long tableLevelTimeoutPerTaskMs = 120_000;

    @Value("${thread.processer-level.enabled}")
    protected boolean multiThreadApiLevelEnabled = false;

    // Determine how many pages are processed in a single batch, if page container 10k record each, 3x pages. 30k will be processed
    @Value("${thread.processer-level.batch-size}")
    protected int apiLevelBatchSizeForProcessing = 40;

    // Initial starter number of task - if 20 then the system begin with 20 parallel task
    @Value("${thread.processer-level.initial-concurrency}")
    protected int apiLevelInitialConcurrency = 80;

    // Task max limit, ex: no more than 40 task running in parallel
    @Value("${thread.processer-level.max-concurrency}")
    protected int apiLevelMaxConcurrency = 160;

    // Retry if task failed
    @Value("${thread.processer-level.max-retry}")
    protected int apiLevelMaxRetries = 5;

    // if task hit timeout it will be terminated, 120_000 == 2 min
    @Value("${thread.processer-level.timeout}")
    protected long apiLevelTimeoutPerTaskMs = 600_000;

    private final UniversalDataPersistentDAO universalDataPersistentDAO;
    private final IPollCommonService iPollCommonService;
    private final IS3DataService is3DataService;
    private final EdxNbsOdseDataPersistentDAO edxNbsOdseDataPersistentDAO;
    private final IApiService apiService;


    @Value("${poll.edx_activity.full_sync}")
    protected boolean edxFullSync;
    @Value("${poll.odse.full_sync}")
    protected boolean odseFullSync;

    private boolean tryApiFatal = true;
    public UniversalDataHandlingService(UniversalDataPersistentDAO universalDataPersistentDAO,
                                        IPollCommonService iPollCommonService,
                                        IS3DataService is3DataService,
                                        EdxNbsOdseDataPersistentDAO edxNbsOdseDataPersistentDAO, IApiService apiService) {
        this.universalDataPersistentDAO = universalDataPersistentDAO;
        this.iPollCommonService = iPollCommonService;
        this.is3DataService = is3DataService;
        this.edxNbsOdseDataPersistentDAO = edxNbsOdseDataPersistentDAO;
        this.apiService = apiService;
    }

    @SuppressWarnings({"java:S3776", "java:S6541", "java:S1141"})
    public void handlingExchangedData(String source) throws APIException {
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
            if (config.getTableOrder() == 1 && multiThreadTableLevelEnabled) {
                threadedConfigs.add(config);
            } else {
                sequentialConfigs.add(config);
            }
        }

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
                            pollAndPersistData(pollDataSyncConfig);
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
            pollAndPersistData(pollDataSyncConfig);
        }

    }

    @SuppressWarnings({"java:S1141","java:S3776", "java:S6541"})
    protected void pollAndPersistData(PollDataSyncConfig config) throws APIException {
        try {
            LogResponseModel log;
            Integer totalRecordCounts = 0;

            boolean isInitialLoad = iPollCommonService.checkInitialLoadForIndividualTable(config);

            boolean forceIncrementalLoadApplied = false;
            // Forcing Initial Load when Config said so and these only apply on ODSE and EDX
            if (
                    (edxFullSync && config.getSourceDb().equalsIgnoreCase(NBS_ODSE_EDX)) ||
                            (odseFullSync && config.getSourceDb().equalsIgnoreCase(ODSE_OBS))
            ) {
                isInitialLoad = false; // NOSONAR
            }

            if(config.isRecreateApplied() ) {
                // IF recreated applied, EXPLICITLY set initialLoad to true, so the flow can be rerun
                isInitialLoad = true;
            }
            String timeStampForPoll = "";
            Timestamp timestampWithNull = null;
            String maxId = "";

            if (config.isUseKeyPagination() && hasOnlyOneKey(config.getKeyList())) {
                maxId = getMaxId(isInitialLoad, config.getTableName(), config.getKeyList());
                timestampWithNull = getCurrentTimestamp();
            }
            else {
                timeStampForPoll = getPollTimestamp(isInitialLoad, config.getTableName(), forceIncrementalLoadApplied);
                timestampWithNull = getCurrentTimestamp();
            }

            var startTime = getCurrentTimestamp();

            String logStr = "";


            var totalRecordCountsResponse = apiService.callDataCountEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, config.isUseKeyPagination(), maxId);

            if (!totalRecordCountsResponse.isSuccess()) {
                log = new LogResponseModel(CRITICAL_COUNT_LOG + totalRecordCountsResponse.getApiException().getMessage(), getStackTraceAsString(totalRecordCountsResponse.getApiException()), ERROR, startTime,
                        totalRecordCountsResponse);
                iPollCommonService.updateLogNoTimestamp(config.getTableName(), log);
                throw totalRecordCountsResponse.getApiException();
            }



            totalRecordCounts = totalRecordCountsResponse.getResponse();
            int batchSize = pullLimit;
            int totalPages = (int) Math.ceil((double)  totalRecordCounts / batchSize);

            if (!config.isNoPagination()) {
                var encodedDataWithNullResponse = apiService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, true,
                        "0", "0", false, config.isUseKeyPagination(), maxId);
                if (!encodedDataWithNullResponse.isSuccess()) {
                    log = new LogResponseModel(CRITICAL_COUNT_LOG + encodedDataWithNullResponse.getApiException().getMessage(),
                            getStackTraceAsString(encodedDataWithNullResponse.getApiException()), ERROR, startTime, encodedDataWithNullResponse);
                    iPollCommonService.updateLogNoTimestamp(config.getTableName(), log);
                    throw encodedDataWithNullResponse.getApiException();
                }

                try {
                    var rawJsonDataWithNull = encodedDataWithNullResponse.getResponse();
                    if (storeJsonInS3) {
                        log = is3DataService.persistToS3MultiPart(config.getSourceDb(), rawJsonDataWithNull, config.getTableName(),
                                timestampWithNull, isInitialLoad, encodedDataWithNullResponse);
                        log.setStartTime(startTime);
                        log.setLog(S3_LOG + (log.getLog() == null || log.getLog().isEmpty()? SUCCESS : log.getLog()));
                        log.setStatus(log.getStatus().equalsIgnoreCase(ERROR)? log.getStatus() : SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestampWithNull, log);
                    }
                    else if (storeInSql) {
                        if (config.getSourceDb().equalsIgnoreCase(NBS_ODSE_EDX)) {
                            log =  edxNbsOdseDataPersistentDAO.saveNbsOdseData(config.getTableName(), rawJsonDataWithNull, encodedDataWithNullResponse);
                        }
                        else {
                            log =  universalDataPersistentDAO.saveUniversalData(config, rawJsonDataWithNull, isInitialLoad, startTime, encodedDataWithNullResponse);
                        }
                        log.setStartTime(startTime);
                        log.setLog(SQL_LOG + (log.getLog() == null ||log.getLog().isEmpty()? SUCCESS : log.getLog()));
                        log.setStatus(log.getStatus().equalsIgnoreCase(ERROR)? log.getStatus() : SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestampWithNull, log);
                    }
                    else  {
                        log = iPollCommonService.writeJsonDataToFile(config.getSourceDb(), config.getTableName(), timestampWithNull, rawJsonDataWithNull, encodedDataWithNullResponse);
                        log.setStartTime(startTime);
                        log.setLog(LOCAL_DIR_LOG + (log.getLog() == null ||log.getLog().isEmpty()? SUCCESS : log.getLog()));
                        log.setStatus(log.getStatus().equalsIgnoreCase(ERROR)? log.getStatus() : SUCCESS);
                        iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestampWithNull, log);
                    }
                }
                catch (Exception e) {
                    log = new LogResponseModel(CRITICAL_NULL_LOG + e.getMessage(), getStackTraceAsString(e), ERROR, startTime, encodedDataWithNullResponse);
                    iPollCommonService.updateLogNoTimestamp(config.getTableName(), log);
                    throw new DataPollException("TASK FAILED: " + getStackTraceAsString(e));
                }


                if (totalRecordCounts >= THREAD_CHECK && multiThreadApiLevelEnabled)
                {
                    processingDataBatchMultiThreadSemaphore( totalPages,  batchSize,  isInitialLoad,  timeStampForPoll,
                            config,  logStr, startTime, totalRecordCounts, maxId);
                } else {
                    processingDataBatch( totalPages,  batchSize,  isInitialLoad,  timeStampForPoll,
                         config,  logStr, startTime, maxId, totalRecordCounts);
                }

            }
            else
            {
                String rawJsonData = "";
                Timestamp timestamp = null;
                ApiResponseModel<String> encodedDataResponse = new ApiResponseModel<>();
                try {

                    String encodedData;
                    encodedDataResponse = apiService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, false,
                            "0", "0", true, config.isUseKeyPagination(), maxId);

                    if (!encodedDataResponse.isSuccess()) {
                        log = new LogResponseModel(CRITICAL_COUNT_LOG + encodedDataResponse.getApiException().getMessage(),
                                getStackTraceAsString(encodedDataResponse.getApiException()), ERROR, startTime, encodedDataResponse);
                        iPollCommonService.updateLogNoTimestamp(config.getTableName(), log);
                        throw encodedDataResponse.getApiException();
                    }

                    encodedData = encodedDataResponse.getResponse();
                    rawJsonData = encodedData;
                    timestamp = getCurrentTimestamp();
                } catch (Exception e) {
                    logStr = e.getMessage();
                }

                updateDataHelper(encodedDataResponse, timestamp,
                        rawJsonData, isInitialLoad, logStr,
                        startTime, config);
            }
        }
        catch (APIException e) {
            throw new APIException(e.getMessage(), e);
        }
        catch (Exception e) {
            logger.error("TASK failed. tableName: {}, message: {}", config.getTableName(), getStackTraceAsString(e));
        }
    }

    @SuppressWarnings({"java:S3776","java:S107", "java:S135", "java:S1141"})
    protected void processingDataBatchMultiThreadSemaphore(int totalPages, int batchSize, boolean isInitialLoad, String timeStampForPoll,
                                                           PollDataSyncConfig config, String logStr,
                                                           Timestamp startTime, int totalRecordCounts, String maxId) throws APIException {

        logger.info("Starting processing of {} pages (batchSize={}) in batches of {} with concurrency {}-{}",
                totalPages, batchSize, apiLevelBatchSizeForProcessing, apiLevelInitialConcurrency, apiLevelMaxConcurrency);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int start = 0; start < totalPages; start += apiLevelBatchSizeForProcessing) {
                int end = Math.min(start + apiLevelBatchSizeForProcessing, totalPages);
                List<Future<?>> futures = new ArrayList<>(end - start);
                Semaphore semaphore = new Semaphore(apiLevelInitialConcurrency);
                AtomicInteger currentConcurrency = new AtomicInteger(apiLevelInitialConcurrency);
                AtomicInteger errorCount = new AtomicInteger(0);
                AtomicLong totalResponseTime = new AtomicLong(0);
                AtomicInteger taskCount = new AtomicInteger(0);

                logger.info("Processing batch: pages {} to {} ({} pages)", start, end - 1, end - start);

                // Submit tasks for current batch
                for (int i = start; i < end; i++) {
                    final int pageIndex = i;
                    Future<?> future = executor.submit(() -> {
                        boolean permitAcquired;
                        int retries = 0;
                        try {
                            while (retries < apiLevelMaxRetries) {
                                try {
                                    semaphore.acquire();
                                    permitAcquired = true;
                                    long startTimeMs = System.currentTimeMillis();
                                    String rawJsonData;
                                    Timestamp timestamp;

                                    try {
                                        int startRow = pageIndex * batchSize + 1;
                                        int endRow = Math.min((pageIndex + 1) * batchSize, totalRecordCounts ); // Respect total records
                                        logger.debug("Processing page {} (rows {}-{})", pageIndex, startRow, endRow);
                                        String encodedData;

                                        var encodedDataResponse = apiService.callDataExchangeEndpoint(
                                                config.getTableName(), isInitialLoad, timeStampForPoll, false,
                                                String.valueOf(startRow), String.valueOf(endRow), false,
                                                config.isUseKeyPagination(), maxId
                                        );

                                        encodedDataResponse.setLastTotalRecordCount(totalRecordCounts);
                                        encodedDataResponse.setLastTotalPages(totalPages);
                                        encodedDataResponse.setLastBatchSize(batchSize);

                                        if (!encodedDataResponse.isSuccess()) {
                                            var log = new LogResponseModel(CRITICAL_COUNT_LOG
                                                    + encodedDataResponse.getApiException().getMessage(),
                                                    getStackTraceAsString(encodedDataResponse.getApiException()),
                                                    WARNING, startTime, encodedDataResponse);
                                            iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(),startTime , log);
                                            throw encodedDataResponse.getApiException();
                                        }


                                        encodedData = encodedDataResponse.getResponse();
                                        rawJsonData = encodedData;
                                        timestamp = getCurrentTimestamp();

                                        updateDataHelper(encodedDataResponse, timestamp, rawJsonData,
                                                isInitialLoad, logStr, startTime, config);
                                        break; // Success, exit retry loop
                                    }
                                    catch (APIException e) {
                                        throw e;
                                    }
                                    catch (Exception e) {
                                        errorCount.incrementAndGet();
                                        logger.error("Error on page {} (attempt {}/{}): {}", pageIndex, retries + 1, apiLevelMaxRetries, e.getMessage(), e);
                                        if (retries == apiLevelMaxRetries - 1) {
                                            throw e; // Final failure
                                        }
                                        Thread.sleep(2000 * (retries + 1)); // Backoff: 2s, 4s, 6s
                                    } finally {
                                        long responseTime = System.currentTimeMillis() - startTimeMs;
                                        totalResponseTime.addAndGet(responseTime);
                                        int completedTasks = taskCount.incrementAndGet();
                                        if (completedTasks % 3 == 0) { // Adjust more frequently for small batches
                                            adjustConcurrency(semaphore, currentConcurrency, errorCount, totalResponseTime, completedTasks, apiLevelMaxConcurrency);
                                        }
                                        if (permitAcquired) {
                                            semaphore.release();
                                        }
                                    }
                                }
                                catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    logger.warn("Task for page {} interrupted", pageIndex);
                                    break; // Exit on interruption
                                }
                            }
                        }
                        catch (Exception e) {
                            throw new RuntimeException("Task failed after retries for page " + pageIndex, e); //NOSONAR
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
                        future.get();
                         future.get(apiLevelTimeoutPerTaskMs, TimeUnit.MILLISECONDS);
                        completedTasks++;
                    }
                    catch (TimeoutException e) {
                        logger.error("Task for page {} timed out after {} ms", pageIndex, apiLevelTimeoutPerTaskMs);
                        future.cancel(true); // Cancel to free resources
                        failedPages.add(pageIndex); // Mark for reprocessing
                    }

                    catch (InterruptedException e) {
                        logger.warn("Interrupted while waiting for batch completion");
                        Thread.currentThread().interrupt();
                        failedPages.add(pageIndex); // Mark for reprocessing
                        break;
                    } catch (ExecutionException e) {
                        Throwable cause = e.getCause();
                        logger.error("Task for page {} failed: {}", pageIndex, cause.getMessage(), cause);
                        if (cause instanceof RuntimeException) {
                            Throwable rootCause = cause.getCause();
                            if (rootCause instanceof APIException apiException) {
                                throw apiException; // Propagate the nested APIException
                            }
                        } else if (cause instanceof APIException apiException) {
                            throw apiException; // Direct APIException case
                        }
                        failedPages.add(pageIndex);
                    }
                }

                logger.info("Batch completed: {}/{} pages processed (total so far: {})", completedTasks, end - start, start + completedTasks);
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
        }
        catch (APIException e) {
            throw new APIException(e.getMessage(), e);
        }
        catch (Exception e) {
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

    @SuppressWarnings("java:S107")
    protected void processingDataBatch(int totalPages, int batchSize, boolean isInitialLoad, String timeStampForPoll,
                                       PollDataSyncConfig config, String logStr,
                                       Timestamp startTime, String maxId,
                                       Integer totalRecordCount) throws APIException {
        // Total Page count will be zero if no new r
        for (int i = 0; i < totalPages; i++) {
            String rawJsonData = "";
            Timestamp timestamp = null;
            ApiResponseModel<String> responseModel = new ApiResponseModel<>();
            responseModel.setLastTotalRecordCount(totalRecordCount);
            responseModel.setLastTotalPages(totalPages);
            responseModel.setLastBatchSize(batchSize);
            try {
                int startRow = i * batchSize + 1;
                int endRow = (i + 1) * batchSize;

                String encodedData ;
                responseModel = apiService.callDataExchangeEndpoint(config.getTableName(), isInitialLoad, timeStampForPoll, false,
                        String.valueOf(startRow), String.valueOf(endRow), false,
                        config.isUseKeyPagination(), maxId);

                encodedData = responseModel.getResponse();

                rawJsonData = encodedData;
                timestamp = getCurrentTimestamp();
            } catch (Exception e) {
                logStr = e.getMessage();
            }

            updateDataHelper(responseModel, timestamp,
                    rawJsonData, isInitialLoad, logStr,
                    startTime, config);

        }
    }

    protected String getPollTimestamp(boolean isInitialLoad, String tableName, boolean forceIncrementalLoadApplied) {
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

        if (timeStampForPoll.isEmpty()) {
            timeStampForPoll = iPollCommonService.getCurrentTimestamp();
        }

        if (forceIncrementalLoadApplied) {
            timeStampForPoll = iPollCommonService.getCurrentTimestamp();
        }
        return timeStampForPoll;
    }

    protected String getMaxId(boolean isInitialLoad, String tableName, String key) {
        if (isInitialLoad) {
            return "-1";
        }
        else {
            return iPollCommonService.getMaxId(tableName, key);
        }
    }

    @SuppressWarnings({"java:S107","java:S3776", "java:S6541"})
    protected void updateDataHelper(ApiResponseModel<String> apiResponseModel, Timestamp timestamp,
                                    String rawJsonData, boolean isInitialLoad, String log,
                                    Timestamp startTime, PollDataSyncConfig config) throws APIException {
        LogResponseModel logResponseModel = new LogResponseModel(apiResponseModel);

        try {
            if (!apiResponseModel.isSuccess()) {
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

                throw apiResponseModel.getApiException();
            }
            else {
                if (storeJsonInS3) {
                    logResponseModel = is3DataService.persistToS3MultiPart(config.getSourceDb(), rawJsonData, config.getTableName(), timestamp, isInitialLoad, apiResponseModel);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(S3_LOG + (log == null || log.isEmpty()? SUCCESS : log));
                    logResponseModel.setStatus(logResponseModel.getStatus().equalsIgnoreCase(ERROR)? logResponseModel.getStatus() : SUCCESS);
                    iPollCommonService.updateLastUpdatedTimeAndLogS3(config.getTableName(), timestamp, logResponseModel);
                }
                else if (storeInSql)
                {
                    if (config.getSourceDb().equalsIgnoreCase(NBS_ODSE_EDX)) {
                        logResponseModel = edxNbsOdseDataPersistentDAO.saveNbsOdseData(config.getTableName(), rawJsonData, apiResponseModel);
                    }
                    else {
                        logResponseModel = universalDataPersistentDAO.saveUniversalData(config, rawJsonData, isInitialLoad, startTime, apiResponseModel);

                    }
                    logResponseModel.setStartTime(startTime);

                    if (!logResponseModel.getStatus().equalsIgnoreCase(WARNING)) {
                        logResponseModel.setLog(SQL_LOG + (log == null || log.isEmpty()? SUCCESS : log));
                        logResponseModel.setStatus(logResponseModel.getStatus().equalsIgnoreCase(ERROR)? logResponseModel.getStatus() : SUCCESS);
                    }

                    iPollCommonService.updateLastUpdatedTimeAndLog(config.getTableName(), timestamp, logResponseModel);
                }
                else
                {
                    logResponseModel = iPollCommonService.writeJsonDataToFile(config.getSourceDb(), config.getTableName(), timestamp, rawJsonData, apiResponseModel);
                    logResponseModel.setStartTime(startTime);
                    logResponseModel.setLog(LOCAL_DIR_LOG + (log == null || log.isEmpty()? SUCCESS : log));
                    logResponseModel.setStatus(logResponseModel.getStatus().equalsIgnoreCase(ERROR)? logResponseModel.getStatus() : SUCCESS);

                    iPollCommonService.updateLastUpdatedTimeAndLogLocalDir(config.getTableName(), timestamp, logResponseModel);
                }
            }
        }
        catch (APIException e) {
            logResponseModel = new LogResponseModel(apiResponseModel);
            logResponseModel.setStatus(ERROR);
            logResponseModel.setStartTime(startTime);
            logResponseModel.setLog(CRITICAL_NON_NULL_LOG + e.getMessage());
            logResponseModel.setStackTrace(getStackTraceAsString(e));
            iPollCommonService.updateLogNoTimestamp(config.getTableName(), logResponseModel);
            throw new APIException(e.getMessage(), e);
        }
        catch (Exception e) {
            logResponseModel = new LogResponseModel(apiResponseModel);
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