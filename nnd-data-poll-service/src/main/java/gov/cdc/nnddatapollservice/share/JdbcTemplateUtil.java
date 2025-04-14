package gov.cdc.nnddatapollservice.share;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.configuration.LocalDateTimeAdapter;
import gov.cdc.nnddatapollservice.configuration.TimestampAdapter;
import gov.cdc.nnddatapollservice.repository.config.PollDataLogRepository;
import gov.cdc.nnddatapollservice.repository.config.model.PollDataLog;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.service.model.dto.TableMetaDataDto;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static gov.cdc.nnddatapollservice.constant.SqlConstantValue.UPDATE;
import static gov.cdc.nnddatapollservice.constant.SqlConstantValue.WHERE_TABLE_NAME;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

@SuppressWarnings("java:S1068")
@Component
public class JdbcTemplateUtil {
    private static Logger logger = LoggerFactory.getLogger(JdbcTemplateUtil.class);
    private final PollDataLogRepository pollDataLogRepository;
    private static final String POLL_CONFIG_TABLE_NAME = "POLL_DATA_SYNC_CONFIG";
    private final JdbcTemplate rdbJdbcTemplate;
    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;
    private final HandleError handleError;
//    private final Gson gsonNorm = new Gson();
    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Value("${thread.jdbc-level.enabled}")
    protected boolean multiThreadJdbcLevelEnabled = false;

    // Initial starter number of task - if 20 then the system begin with 20 parallel task
    @Value("${thread.jdbc-level.initial-concurrency}")
    protected int jdbcLevelInitialConcurrency = 80;

    // Task max limit, ex: no more than 40 task running in parallel
    @Value("${thread.jdbc-level.max-concurrency}")
    protected int jdbcLevelMaxConcurrency = 160;

    // Retry if task failed
    @Value("${thread.jdbc-level.max-retry}")
    protected int jdbcLevelMaxRetry = 1;

    // if task hit timeout it will be terminated, 120_000 == 2 min
    protected long jdbcLevelTimeoutPerTaskMs = 600_000;

    @Value("${thread.jdbc-batch-level.chunk-size}")
    protected int jdbcBatchLevelThreadChunkSize = 2000;
    @Value("${thread.jdbc-batch-level.initial-concurrency}")
    protected int jdbcBatchLevelInitialConcurrency = 5;
    @Value("${thread.jdbc-batch-level.max-concurrency}")
    protected int jdbcBatchLevelMaxConcurrency = 10;
    @Value("${thread.jdbc-batch-level.max-retry}")
    protected int jdbcBatchLevelMaxRetry = 1;
    protected long jdbcBatchLevelTimeoutPerTaskMs = 360_000;


    private final Gson gsonSpec = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
            .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
            .serializeNulls()
            .create();

    public JdbcTemplateUtil(PollDataLogRepository pollDataLogRepository, @Qualifier("rdbJdbcTemplate") JdbcTemplate rdbJdbcTemplate, HandleError handleError) {
        this.pollDataLogRepository = pollDataLogRepository;
        this.rdbJdbcTemplate = rdbJdbcTemplate;
        this.handleError = handleError;
    }

    @SuppressWarnings("java:S1192")
    public void upsertSingle(String tableName, Map<String, Object> data, String keyList) throws SQLException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("No data provided for table: " + tableName);
        }

        if (keyList == null || keyList.trim().isEmpty()) {
            throw new IllegalArgumentException("Key list cannot be null or empty");
        }

        data.entrySet().removeIf(entry -> entry.getKey().equalsIgnoreCase("RowNum"));

        // Extract valid column names from the database
        Set<String> columnNames = getColumnNames(tableName);
        Map<String, Object> validData = new LinkedHashMap<>(data);
        validData.keySet().retainAll(columnNames);

        if (validData.isEmpty()) {
            throw new IllegalArgumentException("No valid columns found for table: " + tableName);
        }

        // Extract column names and generate query parts
        List<String> columnList = new ArrayList<>(validData.keySet());
        String columns = String.join(", ", columnList);
        String placeholders = "(" + String.join(", ", Collections.nCopies(columnList.size(), "?")) + ")";
        String updates = columnList.stream()
                .map(col -> "target." + col + " = source." + col)
                .collect(Collectors.joining(", "));

        String valuesForQuery = columnList.stream()
                .map(col -> "source." + col)
                .collect(Collectors.joining(", "));

        // Build the ON condition dynamically
        String[] keys = keyList.split("\\s*,\\s*");
        StringBuilder condition = new StringBuilder("ON ");
        for (int i = 0; i < keys.length; i++) {
            if (i > 0) {
                condition.append(" AND ");
            }
            condition.append("target.").append(keys[i].trim()).append(" = source.").append(keys[i].trim());
        }

        // Construct the dynamic MERGE statement for a single record
        String sql = "MERGE INTO " + tableName + " AS target " +
                "USING (VALUES " + placeholders + ") AS source(" + columns + ") " +
                condition +
                " WHEN MATCHED THEN UPDATE SET " + updates + " " +
                " WHEN NOT MATCHED THEN INSERT (" + columns + ") VALUES (" + valuesForQuery + ");";

        // Convert potential date strings into proper SQL Date objects
        var values = validData.values().stream()
                .map(this::convertIfDate)
                .toArray();


        try {
            rdbJdbcTemplate.update(sql, values);
        } catch (Exception e) {
            throw new SQLException("Single record upsert failed", e);
        }
    }

    /**
     * Converts a string value into SQL Date if it resembles a valid date format.
     */
    private Object convertIfDate(Object value) {
        if (value instanceof String) {
            String stringValue = (String) value;
            try {
                // Detects dates in MM-DD-YYYY format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate parsedDate = LocalDate.parse(stringValue, formatter);
                return java.sql.Date.valueOf(parsedDate);
            } catch (DateTimeParseException ignored) {
                // If parsing fails, return the original value
            }
        }
        return value;
    }

    @SuppressWarnings("java:S3776")
    public void upsertBatch(String tableName, List<Map<String, Object>> dataList, String keyList) throws SQLException {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        if (keyList == null || keyList.trim().isEmpty()) {
            throw new IllegalArgumentException("Key list cannot be null or empty");
        }

        dataList.forEach(data -> data.remove("RowNum"));
        // Extract valid column names from the data list directly
        Set<String> validColumns = dataList.stream()
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toSet());

        // Filter data and retain only valid columns
        List<Map<String, Object>> validDataList = dataList.stream()
                .map(data -> {
                    Map<String, Object> filteredData = new LinkedHashMap<>(data);
                    filteredData.keySet().retainAll(validColumns);
                    return filteredData.isEmpty() ? null : filteredData;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()); //NOSONAR

        if (validDataList.isEmpty()) {
            throw new IllegalArgumentException("No valid columns found for table: " + tableName);
        }

        List<String> columnList = new ArrayList<>(validDataList.get(0).keySet());
        String columns = String.join(", ", columnList);

        // Prepare query parts
        String placeholdersPerRow = "(" + String.join(", ", Collections.nCopies(columnList.size(), "?")) + ")";
        String updates = columnList.stream()
                .map(col -> "target." + col + " = source." + col)
                .collect(Collectors.joining(", "));
        String valuesForQuery = columnList.stream()
                .map(col -> "source." + col)
                .collect(Collectors.joining(", "));

        String[] keys = keyList.split("\\s*,\\s*");
        StringBuilder condition = new StringBuilder("ON ");
        for (int k = 0; k < keys.length; k++) {
            if (k > 0) {
                condition.append(" AND ");
            }
            condition.append("target.").append(keys[k].trim()).append(" = source.").append(keys[k].trim());
        }

        int maxParamsPerRow = columnList.size();
        int maxRows = 2100 / maxParamsPerRow; // Ensure within SQL Server limit
        int totalRows = validDataList.size();

        try {
            for (int i = 0; i < totalRows; i += maxRows) {
                List<Map<String, Object>> batch = validDataList.subList(i, Math.min(i + maxRows, totalRows));

                // Combine all batch values into a single list
                List<Object> combinedValues = new ArrayList<>();
                for (Map<String, Object> data : batch) {
                    for (String column : columnList) {
                      //  combinedValues.add(data.getOrDefault(column, null)); // Add each column value
                        combinedValues.add(convertIfDate(data.getOrDefault(column, null))); // Convert potential dates

                    }
                }

                // Generate row placeholders dynamically
                String rowPlaceholders = String.join(", ", Collections.nCopies(batch.size(), placeholdersPerRow));




                // Build the MERGE SQL query
                String sql = "MERGE INTO " + tableName + " AS target " +
                        " USING (VALUES " + rowPlaceholders + ") AS source(" + columns + ") " +
                         condition +
                        " WHEN MATCHED THEN UPDATE SET " + updates + " " +
                        " WHEN NOT MATCHED BY TARGET THEN INSERT (" + columns + ") VALUES (" + valuesForQuery + ");";

                // Execute batch update using JdbcTemplate
                rdbJdbcTemplate.update(sql, combinedValues.toArray());
            }
        } catch (Exception e) {
            throw new SQLException("Batch update failed", e);
        }
    }

    @SuppressWarnings({"java:S3776","java:S1141", "java:S1871"})
    public LogResponseModel persistingGenericTable(
            String jsonData,
            PollDataSyncConfig config,
            boolean initialLoad,
            Timestamp startTime,
            ApiResponseModel<?> apiResponseModel) {
        LogResponseModel log = new LogResponseModel(apiResponseModel);
        try {
            if (config.getTableName() != null && !config.getTableName().isEmpty()) {

                SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(rdbJdbcTemplate);
                jdbcInsert = jdbcInsert.withTableName(config.getTableName());
                List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);

                if (records != null && !records.isEmpty()) {
                    try {
                        if (records.size() >  batchSize) {
                            int sublistSize = batchSize;
                            for (int i = 0; i < records.size(); i += sublistSize) {
                                int end = Math.min(i + sublistSize, records.size());
                                List<Map<String, Object>> sublist = records.subList(i, end);
                                if (initialLoad || config.getKeyList() == null || config.getKeyList().isEmpty()) {
                                    sublist.forEach(data -> data.remove("RowNum"));
                                    jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sublist));

                                } else {
                                    upsertBatch(config.getTableName(), sublist, config.getKeyList());
                                }
                            }
                        } else {
                            if (initialLoad || config.getKeyList() == null || config.getKeyList().isEmpty() || config.isRecreateApplied()) {
                                records.forEach(data -> data.remove("RowNum"));
                                jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));

                            } else {
                                upsertBatch(config.getTableName(), records, config.getKeyList());
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        if (multiThreadJdbcLevelEnabled) {
                            log = handleBatchInsertionFailureMultiThread(records, config, jdbcInsert, startTime, apiResponseModel, log);
                        }
                        else {
                            log = handleBatchInsertionFailure(records, config, jdbcInsert, startTime, apiResponseModel, log);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.setLog(e.getMessage());
            log.setStackTrace(getStackTraceAsString(e));
            log.setStatus(ERROR);
            logger.error("Error executeBatch. class: {}, tableName: {}, Error:{}", e.getClass() ,config.getTableName(), e.getMessage());
        }

        if (log.getStatus() != null && !log.getStatus().equals(WARNING)) {
            log.setStatus(SUCCESS);
        } else if (log.getStatus() == null) {
            log.setStatus(SUCCESS);
        }
        return log;
    }

    @SuppressWarnings({"java:S3776", "java:S1141", "java:S1871", "java:S2142"})
    public LogResponseModel persistingGenericTableMultiThread(
            String jsonData,
            PollDataSyncConfig config,
            boolean initialLoad,
            Timestamp startTime,
            ApiResponseModel<?> apiResponseModel) {

        logger.info("Starting virtual-threaded persistence for table '{}'", config.getTableName());

        LogResponseModel log = new LogResponseModel(apiResponseModel);

        Semaphore semaphore = new Semaphore(jdbcBatchLevelInitialConcurrency);
        AtomicInteger currentConcurrency = new AtomicInteger(jdbcBatchLevelInitialConcurrency);

        try {
            if (config.getTableName() != null && !config.getTableName().isEmpty()) {

                SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(rdbJdbcTemplate)
                        .withTableName(config.getTableName());

                List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);

                if (!records.isEmpty()) {
                    int totalRecords = records.size();

                    logger.debug("Processing {} records in batches of {} (chunkSize={}, concurrency={}→{}, timeout={}ms)",
                            totalRecords, batchSize, jdbcBatchLevelThreadChunkSize,
                            jdbcBatchLevelInitialConcurrency, jdbcBatchLevelMaxConcurrency, jdbcBatchLevelTimeoutPerTaskMs);

                    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

                        for (int i = 0; i < totalRecords; i += batchSize) {
                            int end = Math.min(i + batchSize, totalRecords);
                            List<Map<String, Object>> batch = records.subList(i, end);

                            logger.debug("Submitting batch records {} to {} (size = {})", i, end - 1, end - i);

                            List<Future<?>> futures = new ArrayList<>();

                            for (int j = 0; j < batch.size(); j += jdbcBatchLevelThreadChunkSize) {
                                int chunkEnd = Math.min(j + jdbcBatchLevelThreadChunkSize, batch.size());
                                List<Map<String, Object>> chunk = batch.subList(j, chunkEnd);
                                int chunkNumber = j / jdbcBatchLevelThreadChunkSize + 1;

                                futures.add(executor.submit(() -> {
                                    int retries = 0;
                                    boolean acquired = false;

                                    while (retries < jdbcBatchLevelMaxRetry) {
                                        try {
                                            semaphore.acquire();
                                            acquired = true;

                                            if (initialLoad || config.getKeyList() == null || config.getKeyList().isEmpty() || config.isRecreateApplied()) {
                                                chunk.forEach(data -> data.remove("RowNum"));
                                                logger.debug("Chunk {}: Inserting {} records", chunkNumber, chunk.size());
                                                jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(chunk));
                                            } else {
                                                logger.debug("Chunk {}: Upserting {} records", chunkNumber, chunk.size());
                                                upsertBatch(config.getTableName(), chunk, config.getKeyList());
                                            }

                                            logger.info("Chunk {}: Completed successfully", chunkNumber);
                                            break;

                                        } catch (Exception e) {
                                            if (e instanceof DuplicateKeyException){
                                                logger.info("Duplicated Key Found");
                                                break;
                                            }
                                            else {
                                                e.printStackTrace();
                                                retries++;
                                                logger.warn("Chunk {}: Attempt {}/{} failed - {}", chunkNumber, retries, jdbcBatchLevelMaxRetry, e.getMessage());

                                                if (retries >= jdbcBatchLevelMaxRetry) {
                                                    logger.error("Chunk {}: Failed after max retries", chunkNumber, e);
                                                    throw new RuntimeException("Chunk insert failed", e); //NOSONAR
                                                }

                                                try {
                                                    Thread.sleep(2000L * retries); // Exponential backoff
                                                } catch (InterruptedException ex) {
                                                    Thread.currentThread().interrupt();
                                                    throw new RuntimeException("Interrupted during retry sleep", ex); //NOSONAR
                                                }
                                            }

                                        } finally {
                                            if (acquired) semaphore.release();
                                        }
                                    }
                                }));
                            }

                            // Wait for each chunk with timeout
                            for (int idx = 0; idx < futures.size(); idx++) {
                                Future<?> future = futures.get(idx);
                                try {
                                    future.get(jdbcBatchLevelTimeoutPerTaskMs, TimeUnit.MILLISECONDS);
                                }
                                catch (TimeoutException e) {
                                    logger.error("Chunk task {} timed out after {}ms", idx + 1, jdbcBatchLevelTimeoutPerTaskMs);
                                    future.cancel(true);
                                    throw new RuntimeException("Chunk task timeout exceeded", e); //NOSONAR
                                }
                                catch (ExecutionException e) {
                                    throw e; // Already logged inside chunk
                                }
                            }

                            if (currentConcurrency.get() < jdbcBatchLevelMaxConcurrency) {
                                int increased = currentConcurrency.incrementAndGet();
                                semaphore.release();
                                logger.debug("Increased concurrency to {}", increased);
                            }

                            logger.debug("Batch processed: records {} to {}", i, end - 1);
                        }

                        logger.info("All records processed successfully for table '{}'", config.getTableName());

                    } catch (Exception e) {
                        logger.error("Processing failed. Falling back. Reason: {}", e.getMessage(), e);
                        if (multiThreadJdbcLevelEnabled) {
                            log = handleBatchInsertionFailureMultiThread(records, config, jdbcInsert, startTime, apiResponseModel, log);
                        } else {
                            log = handleBatchInsertionFailure(records, config, jdbcInsert, startTime, apiResponseModel, log);
                        }
                    }
                } else {
                    logger.warn("No records to persist for table '{}'", config.getTableName());
                }

            } else {
                logger.warn("Table name is null or empty in config.");
            }

        } catch (Exception e) {
            log.setLog(e.getMessage());
            log.setStackTrace(getStackTraceAsString(e));
            log.setStatus(ERROR);
            logger.error("Unexpected error in virtual-threaded persistence for '{}': {}", config.getTableName(), e.getMessage(), e);
        }

        if (log.getStatus() != null && !log.getStatus().equals(WARNING)) {
            log.setStatus(SUCCESS);
        } else if (log.getStatus() == null) {
            log.setStatus(SUCCESS);
        }

        return log;
    }


    @SuppressWarnings({"java:S3776", "java:S135", "java:S1141", "java:S2142"})
    public LogResponseModel handleBatchInsertionFailureMultiThread(List<Map<String, Object>> records, PollDataSyncConfig config,
                                                        SimpleJdbcInsert simpleJdbcInsert, Timestamp startTime,
                                                        ApiResponseModel<?> apiResponseModel, LogResponseModel log) {
        AtomicLong errorCount = new AtomicLong(0);
        List<String> errors = Collections.synchronizedList(new ArrayList<>());
        Semaphore semaphore = new Semaphore(jdbcLevelInitialConcurrency);
        AtomicReference<Exception> anyErrorException = new AtomicReference<>();
        AtomicBoolean anyFatal = new AtomicBoolean(false);
        AtomicBoolean anyError = new AtomicBoolean(false);


        logger.info("Starting batch insertion with concurrency {}-{}, timeout {} ms",
                jdbcLevelInitialConcurrency, jdbcLevelMaxConcurrency, jdbcLevelTimeoutPerTaskMs);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();

            for (Map<String, Object> res : records) {
                Future<?> future = executor.submit(() -> {
                    boolean acquired = false;
                    int retries = 0;

                    try {
                        while (retries < jdbcLevelMaxRetry) {
                            try {
                                semaphore.acquire();
                                acquired = true;

                                long startTimeMs = System.currentTimeMillis();

                                if (config.getKeyList() == null || config.getKeyList().isEmpty() || config.isRecreateApplied()) {
                                    simpleJdbcInsert.execute(new MapSqlParameterSource(res));
                                } else {
                                    upsertSingle(config.getTableName(), res, config.getKeyList());
                                }

                                long duration = System.currentTimeMillis() - startTimeMs;
                                if (duration > jdbcLevelTimeoutPerTaskMs) {
                                    throw new TimeoutException("Task exceeded timeout of " + jdbcLevelTimeoutPerTaskMs + " ms");
                                }
                                break; // Success, exit retry loop
                            }
                            catch (TimeoutException e) {
                                logger.error("Timeout Error Occurred");
                                errorCount.incrementAndGet();
                                errors.add(e.getMessage());
                                break; // No retry on timeout
                            }
                            catch (Exception e) {
                                errorCount.incrementAndGet();
                                anyError.set(true);

                                if (errors.isEmpty()) {
                                    errors.add(e.getMessage());
                                }
                                Throwable existing = anyErrorException.get();
                                if (existing == null || !existing.getClass().equals(e.getClass())) {
                                    errors.add(e.getMessage());
                                }
                                anyErrorException.set(e);

                                retries++;
                                if (retries < jdbcLevelMaxRetry) {
                                    Thread.sleep(2000L * (retries + 1)); // Exponential backoff
                                } else {
                                    anyFatal.set(true);
                                    break;
                                }
                            } finally {
                                if (acquired) semaphore.release();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.warn("Task interrupted for record: {}",
                                //gsonNorm.toJson(res)
                                gsonSpec.toJson(res)
                        );
                    }
                });
                futures.add(future);
            }

            // Wait for all tasks to complete with timeout
            for (Future<?> future : futures) {
                try {
                    future.get(jdbcLevelTimeoutPerTaskMs, TimeUnit.MILLISECONDS);
                }
                catch (TimeoutException e) {
                    logger.error("Task timeout after {} ms", jdbcLevelTimeoutPerTaskMs);
                    errorCount.incrementAndGet();
                }
                catch (Exception e) {
                    logger.error("Unexpected error in batch execution: {}", e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Executor failure: {}", e.getMessage(), e);
        }

        // Final log update
        if (errorCount.get() == 0) {
            log.setStatus(SUCCESS);
        } else {
            log.setStatus(anyFatal.get() ? ERROR : WARNING);
            log.setLog(errorCount.get() + " Issues occurred during UPSERT/SINGLE INSERTION");
            log.setStackTrace(new Gson().toJson(errors));
        }

        return log;
    }


    @SuppressWarnings({"java:S3776", "java:S1481", "java:S125"})
    public LogResponseModel handleBatchInsertionFailure(List<Map<String, Object>> records, PollDataSyncConfig config,
                                            SimpleJdbcInsert simpleJdbcInsert, Timestamp startTime,
                                            ApiResponseModel<?> apiResponseModel, LogResponseModel log) {
        boolean anyError = false;
        boolean anyFatal = false;
        List<String> errors = new ArrayList<>();
        Exception anyErrorException = null;
        Long errorCount = 0L;

        for (Map<String, Object> res : records) {
            try {
                if (config.getKeyList() == null  || config.getKeyList().isEmpty() || config.isRecreateApplied()) {
                    simpleJdbcInsert.execute(new MapSqlParameterSource(res));
                }
                else
                {
                    upsertSingle(config.getTableName(), res, config.getKeyList());
                }
            } catch (Exception ei) {
                logger.debug("Error occurred during UPSERT/SINGLE INSERTION", ei);
                ++errorCount;
                anyError= true;
                if (anyErrorException == null) {
                    errors.add(ei.getMessage());
                }
                if (anyErrorException != null && !anyErrorException.getClass().equals(ei.getClass())) {
                    errors.add(ei.getMessage());
                }
                anyErrorException = ei;
//
//                if (!config.getTableName().equalsIgnoreCase("PERSON")) {
//                    logger.error("ERROR occurred at record: {}, {}", gsonNorm.toJson(res), ei.getMessage()); // NOSONAR
//                }
//                LogResponseModel logModel = new LogResponseModel(
//                        ei.getMessage(),getStackTraceAsString(ei),
//                        ERROR, startTime, apiResponseModel);
//                updateLog(config.getTableName(), logModel);
//                handleError.writeRecordToFile(config.getTableName().equalsIgnoreCase("PERSON")
//                            ? gsonSpec
//                            : gsonNorm, res,
//                        config.getTableName() + UUID.randomUUID(),
//                        sqlErrorPath
//                                + "/" + config.getSourceDb() + "/"
//                                + ei.getClass().getSimpleName()
//                                + "/" + config.getTableName() + "/");

            }
        }

        if (!anyError)
        {
            log.setStatus(SUCCESS);
        }
        else {
            Gson gson = new Gson();
            String jsonString = gson.toJson(errors);
//            if (anyFatal) {
//                log.setStatus(ERROR);
//            } else {
//                log.setStatus(WARNING);
//            }
            log.setStatus(WARNING);
            log.setLog(errorCount + " Records have failed at resolver level");
            log.setStackTrace(jsonString);
        }
        return log;
    }


    private Set<String> getColumnNames(String tableName) throws SQLException {
        Set<String> columnNames = new HashSet<>();

        try (Connection connection = Objects.requireNonNull(rdbJdbcTemplate.getDataSource()).getConnection();
             ResultSet columns = connection.getMetaData().getColumns(null, null, tableName, null)) {

            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
        }
        return columnNames;
    }

    public void deleteTable(String tableName) {
        try {
            String deleteSql = "delete FROM " + tableName;
            rdbJdbcTemplate.execute(deleteSql);
        } catch (Exception e) {
            logger.error("RDB_MODERN:Error in deleting table:{}", e.getMessage());
        }
    }

    public void updateLastUpdatedTime(String tableName, Timestamp timestamp) {
        String updateSql = UPDATE + POLL_CONFIG_TABLE_NAME + " set last_update_time =? where table_name=?;";
        rdbJdbcTemplate.update(updateSql, timestamp, tableName);
    }

    public void updateLog(String tableName, LogResponseModel logResponseModel) {
        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLastUpdatedTimeAndLog(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        String updateSql;
        if (!logResponseModel.apiResponseModel.isSuccess()) {
            updateSql = UPDATE + POLL_CONFIG_TABLE_NAME + " set last_update_time =?, api_fatal_on_last_run = 1 where table_name=?;";
        }
        else {
            updateSql = UPDATE + POLL_CONFIG_TABLE_NAME + " set last_update_time =? where table_name=?;";
        }
        rdbJdbcTemplate.update(updateSql, timestamp, tableName);

        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLastUpdatedTimeAndLogS3(String tableName, Timestamp timestamp,  LogResponseModel logResponseModel) {
        String updateSql;
        if (!logResponseModel.apiResponseModel.isSuccess()) {
            updateSql = UPDATE + POLL_CONFIG_TABLE_NAME + " set last_update_time_s3 =?, api_fatal_on_last_run = 1 where table_name=?;";
        }
        else {
            updateSql = UPDATE + POLL_CONFIG_TABLE_NAME + " set last_update_time_s3 =? where table_name=?;";
        }

        rdbJdbcTemplate.update(updateSql, timestamp, tableName);

        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLastUpdatedTimeAndLogLocalDir(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        String updateSql;
        if (!logResponseModel.apiResponseModel.isSuccess()) {
            updateSql = UPDATE + POLL_CONFIG_TABLE_NAME + " set last_update_time_local_dir =?, api_fatal_on_last_run = 1 where table_name=?;";
        }
        else {
            updateSql = UPDATE + POLL_CONFIG_TABLE_NAME + " set last_update_time_local_dir =? where table_name=?;";
        }

        rdbJdbcTemplate.update(updateSql, timestamp, tableName);

        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLogNoTimestamp(String tableName, LogResponseModel logResponseModel) {
        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public String getLastUpdatedTime(String tableName) {
        String sql = "select last_update_time from " + POLL_CONFIG_TABLE_NAME + WHERE_TABLE_NAME;
        String updatedTime = "";
        Timestamp lastTime = rdbJdbcTemplate.queryForObject(
                sql,
                Timestamp.class, tableName);
        if (lastTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            updatedTime = formatter.format(lastTime); // NOSONAR
        }
        return updatedTime;
    }

    public String getLastUpdatedTimeS3(String tableName) {
        String sql = "select last_update_time_s3 from " + POLL_CONFIG_TABLE_NAME + WHERE_TABLE_NAME;
        String updatedTime = "";
        Timestamp lastTime = rdbJdbcTemplate.queryForObject(
                sql,
                Timestamp.class, tableName);
        if (lastTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            updatedTime = formatter.format(lastTime); // NOSONAR
        }
        return updatedTime;
    }

    public String getLastUpdatedTimeLocalDir(String tableName) {
        String sql = "select last_update_time_local_dir from " + POLL_CONFIG_TABLE_NAME + WHERE_TABLE_NAME;
        String updatedTime = "";
        Timestamp lastTime = rdbJdbcTemplate.queryForObject(
                sql,
                Timestamp.class, tableName);
        if (lastTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            updatedTime = formatter.format(lastTime); // NOSONAR
        }
        return updatedTime;
    }

    public String getMaxId(String tableName, String key) {
        String sql = "SELECT COALESCE(MAX(" + key + "), '0') FROM " + tableName + ";";
        return rdbJdbcTemplate.queryForObject(sql, String.class);
    }




    public List<PollDataSyncConfig> getTableListFromConfig() {
        String sql = "select * from " + POLL_CONFIG_TABLE_NAME;
        List<PollDataSyncConfig> tableList = rdbJdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(PollDataSyncConfig.class));
        return tableList;
    }


    @SuppressWarnings("java:S6204")
    public List<TableMetaDataDto> getOnPremMetaData(String tableName) {
        String query = """
                SELECT 
                    COLUMN_NAME, 
                    DATA_TYPE, 
                    CHARACTER_MAXIMUM_LENGTH AS MaxLength, 
                    IS_NULLABLE, 
                    COLUMN_DEFAULT 
                FROM INFORMATION_SCHEMA.COLUMNS 
                WHERE TABLE_NAME = ? 
                AND TABLE_SCHEMA = 'dbo'
            """;
        return rdbJdbcTemplate.queryForList(query, tableName).stream()
                .map(this::mapToTableMetaDataDto)
                .collect(Collectors.toList());
    }

    private TableMetaDataDto mapToTableMetaDataDto(Map<String, Object> row) {
        TableMetaDataDto dto = new TableMetaDataDto();
        dto.setColumnName((String) row.get("COLUMN_NAME"));
        dto.setDataType((String) row.get("DATA_TYPE"));
        dto.setCharacterMaximumLength(row.get("CHARACTER_MAXIMUM_LENGTH") != null
                ? ((Number) row.get("CHARACTER_MAXIMUM_LENGTH")).intValue() : null);
        dto.setIsNullable((String) row.get("IS_NULLABLE"));
        dto.setColumnDefault((String) row.get("COLUMN_DEFAULT"));
        return dto;
    }

    public void addColumnsToTable(String tableName, List<TableMetaDataDto> columns) {
        if (columns == null || columns.isEmpty()) {
            logger.warn("No columns provided to add to table {}", tableName);
            return;
        }

        StringBuilder query = new StringBuilder("ALTER TABLE ").append(tableName).append(" ADD ");

        // Build the column addition statements
        for (int i = 0; i < columns.size(); i++) {
            TableMetaDataDto column = columns.get(i);

            query.append(column.getColumnName()).append(" ").append(column.getDataType());

            // Append max length if applicable
            if (column.getCharacterMaximumLength() != null &&
                    column.getCharacterMaximumLength() > 0 &&
                    isLengthRequired(column.getDataType())) {
                query.append("(").append(column.getCharacterMaximumLength()).append(")");
            }

            // Add NULL / NOT NULL constraint
            query.append(column.getIsNullable().equalsIgnoreCase("NO") ? " NOT NULL" : " NULL");

            // Add DEFAULT value if provided
            if (column.getColumnDefault() != null && !column.getColumnDefault().isEmpty()) {
                query.append(" DEFAULT '").append(column.getColumnDefault()).append("'");
            }

            // Add a comma between column definitions (except for the last one)
            if (i < columns.size() - 1) {
                query.append(", ");
            }
        }

        // Execute the ALTER TABLE query
        rdbJdbcTemplate.execute(query.toString());
        logger.info("Columns added to table {}: {}", tableName, columns.stream()
                .map(TableMetaDataDto::getColumnName)
                .toList());
    }

    // Helper method to check if a data type requires a length specification
    private boolean isLengthRequired(String dataType) {
        return dataType.equalsIgnoreCase("VARCHAR") ||
                dataType.equalsIgnoreCase("NVARCHAR") ||
                dataType.equalsIgnoreCase("CHAR") ||
                dataType.equalsIgnoreCase("NCHAR");
    }
}
