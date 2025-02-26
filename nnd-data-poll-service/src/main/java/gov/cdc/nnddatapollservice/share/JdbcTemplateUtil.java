package gov.cdc.nnddatapollservice.share;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.repository.config.PollDataLogRepository;
import gov.cdc.nnddatapollservice.repository.config.model.PollDataLog;
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.ERROR;
import static gov.cdc.nnddatapollservice.constant.ConstantValue.SUCCESS;
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

        var values = validData.values().toArray();

        try {
            rdbJdbcTemplate.update(sql, values);
        } catch (Exception e) {
            throw new SQLException("Single record upsert failed", e);
        }
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
                        combinedValues.add(data.getOrDefault(column, null)); // Add each column value
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
            boolean initialLoad) {
        LogResponseModel log = new LogResponseModel();
        try {
            if (config.getTableName() != null && !config.getTableName().isEmpty()) {

                SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(rdbJdbcTemplate);
                jdbcInsert = jdbcInsert.withTableName(config.getTableName());
                List<Map<String, Object>> records = PollServiceUtil.jsonToListOfMap(jsonData);

                if (records != null && !records.isEmpty()) {

                    Set<String> specialTables = new HashSet<>(Arrays.asList(
                            "D_INV_HIV",
                            "D_INV_ADMINISTRATIVE",
                            "D_INV_EPIDEMIOLOGY",
                            "D_INV_LAB_FINDING",
                            "D_INV_MEDICAL_HISTORY",
                            "D_INV_RISK_FACTOR",
                            "D_INV_TREATMENT",
                            "D_INV_VACCINATION"
                    ));



                    if (specialTables.contains(config.getTableName().toUpperCase()))
                    {
                        handleSpecialTableFiltering(config, records, jdbcInsert, initialLoad);
                    }
                    else {
                        try {
                            if (records.size() >  batchSize) {
                                int sublistSize = batchSize;
                                for (int i = 0; i < records.size(); i += sublistSize) {
                                    int end = Math.min(i + sublistSize, records.size());
                                    List<Map<String, Object>> sublist = records.subList(i, end);
                                    if (initialLoad || config.getKeyList().isEmpty()) {
                                        sublist.forEach(data -> data.remove("RowNum"));
                                        jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sublist));

                                    } else {
                                        upsertBatch(config.getTableName(), sublist, config.getKeyList());
                                    }
                                }
                            } else {
                                if (initialLoad || config.getKeyList().isEmpty()) {
                                    records.forEach(data -> data.remove("RowNum"));
                                    jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));

                                } else {
                                    upsertBatch(config.getTableName(), records, config.getKeyList());
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            handleBatchInsertionFailure(records, config, jdbcInsert);
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

        log.setStatus(SUCCESS);
        return log;
    }


    // Helper method to handle filtering for special tables
    @SuppressWarnings("java:S3776")
    public void handleSpecialTableFiltering(PollDataSyncConfig config, List<Map<String, Object>> records, SimpleJdbcInsert simpleJdbcInsert, boolean initialLoad) {
        String keyColumn = config.getTableName() + "_KEY"; // Assuming each table has a key column with the pattern [table]_KEY

        // Query only the existing keys where the key is 1
        String query = "SELECT " + keyColumn + " FROM " + config.getTableName() + " WHERE " + keyColumn + " = 1";
        List<Double> existingKeys = new ArrayList<>(rdbJdbcTemplate.queryForList(query, Double.class));
        logger.info("Found {} existing keys in {} with {} = 1", existingKeys.size(), config.getTableName(), keyColumn);

        Double foundKey = existingKeys.isEmpty()? null : existingKeys.get(0);
        // Filter out records that have a matching key of 1
        List<Map<String, Object>> filteredRecords = records
                .stream()
                .filter(rec -> {
                    // Check if the record contains the keyColumn
                    if (rec.containsKey(keyColumn)) {
                        // Get the value of the keyColumn from the record
                        Object recordKey = rec.get(keyColumn);
                        // Filter out the record if it matches the foundKey
                        return !recordKey.equals(foundKey);
                    }
                    // If the record doesn't contain keyColumn, keep it
                    return true;
                })
                .toList(); //NOSONAR
        logger.info("After filtering, Records size: {}", filteredRecords.size());

        // Additional logic block to remove records with duplicate keys based on keyColumn
        Set<Object> uniqueKeys = new HashSet<>();
        List<Map<String, Object>> deduplicatedRecords = new ArrayList<>();

        for (Map<String, Object> rec : filteredRecords) {
            Object recordKey = rec.get(keyColumn);

            // If the keyColumn is present and the key hasn't been seen yet, keep the record
            if (recordKey != null && uniqueKeys.add(recordKey)) {
                deduplicatedRecords.add(rec);
            }
        }


        if (!deduplicatedRecords.isEmpty()) {
            try {
                if (deduplicatedRecords.size() > batchSize) {
                    int sublistSize = batchSize;
                    for (int i = 0; i < deduplicatedRecords.size(); i += sublistSize) {
                        int end = Math.min(i + sublistSize, deduplicatedRecords.size());
                        List<Map<String, Object>> sublist = deduplicatedRecords.subList(i, end);
                        if (initialLoad || config.getKeyList().isEmpty()) {
                            sublist.forEach(data -> data.remove("RowNum"));
                            simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(sublist));

                        } else {
                            upsertBatch(config.getTableName(), sublist, config.getKeyList());
                        }
                    }
                } else {
                    if (initialLoad || config.getKeyList().isEmpty()) {
                        records.forEach(data -> data.remove("RowNum"));
                        simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));

                    } else {
                        upsertBatch(config.getTableName(), records, config.getKeyList());
                    }
                }
            } catch (Exception e) {
                handleBatchInsertionFailure(records, config, simpleJdbcInsert);
            }
        } else {
            logger.info("No new records to insert for {}.", config.getTableName());
        }
    }

    public void handleBatchInsertionFailure(List<Map<String, Object>> records, PollDataSyncConfig config, SimpleJdbcInsert simpleJdbcInsert) {
        for (Map<String, Object> res : records) {
            try {
                if (config.getKeyList().isEmpty()) {
                    simpleJdbcInsert.execute(new MapSqlParameterSource(res));
                }
                else
                {
                    upsertSingle(config.getTableName(), res, config.getKeyList());
                }
            } catch (Exception ei) {
                if (ei instanceof DataIntegrityViolationException) // NOSONAR
                {
                    logger.debug("Duplicated Key Exception Resolved");
                }
                else {
                    logger.error("ERROR occurred at record: {}, {}", gsonNorm.toJson(res), ei.getMessage()); // NOSONAR
                    handleError.writeRecordToFile(gsonNorm, res,
                            config.getTableName() + UUID.randomUUID(),
                            sqlErrorPath
                                    + "/" + config.getSourceDb() + "/"
                                    + ei.getClass().getSimpleName()
                                    + "/" + config.getTableName() + "/");
                }

            }
        }
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


    public void updateLastUpdatedTimeAndLog(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        String updateSql = "update " + pollConfigTableName + " set last_update_time =? where table_name=?;";
        rdbJdbcTemplate.update(updateSql, timestamp, tableName);

        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLastUpdatedTimeAndLogS3(String tableName, Timestamp timestamp,  LogResponseModel logResponseModel) {
        String updateSql = "update " + pollConfigTableName + " set last_update_time_s3 =? where table_name=?;";
        rdbJdbcTemplate.update(updateSql, timestamp, tableName);

        PollDataLog pollDataLog = new PollDataLog(logResponseModel, tableName);
        pollDataLogRepository.save(pollDataLog);
    }

    public void updateLastUpdatedTimeAndLogLocalDir(String tableName, Timestamp timestamp, LogResponseModel logResponseModel) {
        String updateSql = "update " + pollConfigTableName + " set last_update_time_local_dir =? where table_name=?;";
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




    public List<PollDataSyncConfig> getTableListFromConfig() {
        String sql = "select * from " + pollConfigTableName;
        List<PollDataSyncConfig> tableList = rdbJdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(PollDataSyncConfig.class));
        return tableList;
    }

}
