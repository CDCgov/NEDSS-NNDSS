package gov.cdc.nnddatapollservice.share;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.configuration.LocalDateTimeAdapter;
import gov.cdc.nnddatapollservice.configuration.TimestampAdapter;
import gov.cdc.nnddatapollservice.repository.config.PollDataLogRepository;
import gov.cdc.nnddatapollservice.repository.config.model.PollDataLog;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import gov.cdc.nnddatapollservice.universal.dto.PollDataSyncConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.*;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

@Component
public class JdbcTemplateUtil {
    private static Logger logger = LoggerFactory.getLogger(JdbcTemplateUtil.class);
    private final PollDataLogRepository pollDataLogRepository;
    private final String pollConfigTableName = "POLL_DATA_SYNC_CONFIG";
    private final JdbcTemplate rdbJdbcTemplate;
    @Value("${datasync.data_sync_batch_limit}")
    protected Integer batchSize = 1000;
    private final HandleError handleError;
    private final Gson gsonNorm = new Gson();
    @Value("${datasync.sql_error_handle_log}")
    protected String sqlErrorPath = "";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";


    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();

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

//        var values = validData.values().toArray();
        // Convert potential date strings into proper SQL Date objects
        var values = validData.values().stream()
                .map(this::convertIfDate)
                .toArray();
        rdbJdbcTemplate.update(sql, values);
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

    @SuppressWarnings({"java:S3776","java:S1141"})
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

//                    if (SPECIAL_TABLES.contains(config.getTableName().toUpperCase()))
//                    {
//                        handleSpecialTableFiltering(config, records, jdbcInsert, initialLoad, startTime);
//                    }
//                    else {
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
                           log = handleBatchInsertionFailure(records, config, jdbcInsert, startTime, apiResponseModel, log);
                        }
//                    }

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


    public LogResponseModel handleBatchInsertionFailure(List<Map<String, Object>> records, PollDataSyncConfig config,
                                            SimpleJdbcInsert simpleJdbcInsert, Timestamp startTime,
                                            ApiResponseModel<?> apiResponseModel, LogResponseModel log) {
        boolean anyError = false;
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
//                if (ei instanceof DataIntegrityViolationException) // NOSONAR
//                {
//                    logger.debug("Key Exception Resolved");
//                }
//                else {
                    ++errorCount;
                    anyError= true;
                    if (anyErrorException == null) {
                        errors.add(ei.getMessage());
                    }
                    if (anyErrorException != null && !anyErrorException.getClass().equals(ei.getClass())) {
                        errors.add(ei.getMessage());
                    }
                    anyErrorException = ei;

//                    if (!config.getTableName().equalsIgnoreCase("PERSON")) {
//                        logger.debug("ERROR occurred at record: {}, {}", gsonNorm.toJson(res), ei.getMessage()); // NOSONAR
//                    }

//                    LogResponseModel logModel = new LogResponseModel(
//                            ei.getMessage(),getStackTraceAsString(ei),
//                            ERROR, startTime, apiResponseModel);
//                    updateLog(config.getTableName(), logModel);
//                    handleError.writeRecordToFile(config.getTableName().equalsIgnoreCase("PERSON")
//                                    ? gsonSpec
//                                    : gsonNorm, res,
//                            config.getTableName() + UUID.randomUUID(),
//                            sqlErrorPath
//                                    + "/" + config.getSourceDb() + "/"
//                                    + ei.getClass().getSimpleName()
//                                    + "/" + config.getTableName() + "/");
//                }

            }
        }

        if (!anyError)
        {
            log.setStatus(SUCCESS);
        }
        else {
            Gson gson = new Gson();
            String jsonString = gson.toJson(errors);
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
        String updateSql = "update " + pollConfigTableName + " set last_update_time =? where table_name=?;";
        rdbJdbcTemplate.update(updateSql, timestamp, tableName);
    }

    public void updateLog(String tableName, LogResponseModel logResponseModel) {
        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLastUpdatedTimeAndLog(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        String updateSql;
        if (!logResponseModel.apiResponseModel.isSuccess()) {
            updateSql = "update " + pollConfigTableName + " set last_update_time =?, api_fatal_on_last_run = 1 where table_name=?;";
        }
        else {
            updateSql = "update " + pollConfigTableName + " set last_update_time =? where table_name=?;";
        }
        rdbJdbcTemplate.update(updateSql, timestamp, tableName);

        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLastUpdatedTimeAndLogS3(String tableName, Timestamp timestamp,  LogResponseModel logResponseModel) {
        String updateSql;
        if (!logResponseModel.apiResponseModel.isSuccess()) {
            updateSql = "update " + pollConfigTableName + " set last_update_time_s3 =?, api_fatal_on_last_run = 1 where table_name=?;";
        }
        else {
            updateSql = "update " + pollConfigTableName + " set last_update_time_s3 =? where table_name=?;";
        }

        rdbJdbcTemplate.update(updateSql, timestamp, tableName);

        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLastUpdatedTimeAndLogLocalDir(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        String updateSql;
        if (!logResponseModel.apiResponseModel.isSuccess()) {
            updateSql = "update " + pollConfigTableName + " set last_update_time_local_dir =?, api_fatal_on_last_run = 1 where table_name=?;";
        }
        else {
            updateSql = "update " + pollConfigTableName + " set last_update_time_local_dir =? where table_name=?;";
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
        String sql = "select last_update_time from " + pollConfigTableName + " where table_name=?";
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
        String sql = "select last_update_time_s3 from " + pollConfigTableName + " where table_name=?";
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
        String sql = "select last_update_time_local_dir from " + pollConfigTableName + " where table_name=?";
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
        String sql = "select * from " + pollConfigTableName;
        List<PollDataSyncConfig> tableList = rdbJdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(PollDataSyncConfig.class));
        return tableList;
    }

}
