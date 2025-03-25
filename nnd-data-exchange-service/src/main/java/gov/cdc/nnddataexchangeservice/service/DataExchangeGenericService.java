package gov.cdc.nnddataexchangeservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddataexchangeservice.configuration.TimestampAdapter;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.rdb.DataSyncConfigRepository;
import gov.cdc.nnddataexchangeservice.repository.rdb.DataSyncLogRepository;
import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataSyncConfig;
import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataSyncLog;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import gov.cdc.nnddataexchangeservice.shared.DataSimplification;
import gov.cdc.nnddataexchangeservice.shared.MetricCollector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static gov.cdc.nnddataexchangeservice.constant.DataSyncConstant.*;
import static gov.cdc.nnddataexchangeservice.shared.TimestampHandler.getCurrentTimeStamp;

@Service
public class DataExchangeGenericService implements IDataExchangeGenericService {

    private final DataSyncConfigRepository dataSyncConfigRepository;
    private final DataSyncLogRepository dataSyncLogRepository;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcTemplate srteJdbcTemplate;
    private final JdbcTemplate rdbModernJdbcTemplate;
    private final JdbcTemplate odseJdbcTemplate;
    private final Gson gson;
    @Value("${service.timezone}")
    private String tz = "UTC";
    public DataExchangeGenericService(DataSyncConfigRepository dataSyncConfigRepository, DataSyncLogRepository dataSyncLogRepository,
                                      @Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate,
                                      @Qualifier("srteJdbcTemplate")  JdbcTemplate srteJdbcTemplate,
                                      @Qualifier("rdbModernJdbcTemplate")  JdbcTemplate rdbModernJdbcTemplate,
                                      @Qualifier("odseJdbcTemplate") JdbcTemplate odseJdbcTemplate) {
        this.dataSyncConfigRepository = dataSyncConfigRepository;
        this.dataSyncLogRepository = dataSyncLogRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.srteJdbcTemplate = srteJdbcTemplate;
        this.rdbModernJdbcTemplate = rdbModernJdbcTemplate;
        this.odseJdbcTemplate = odseJdbcTemplate;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
                .serializeNulls()
                .create();
    }

    public Integer getTotalRecord(String tableName, boolean initialLoad, String param, boolean useKeyPagination) throws DataExchangeException {
        DataSyncConfig dataConfig = getConfigByTableName(tableName);

        String query = prepareQuery(dataConfig.getQueryCount(), initialLoad, param, useKeyPagination);

        return executeQueryForTotalRecords(query, dataConfig.getSourceDb());
    }

    private DataSyncConfig getConfigByTableName(String tableName) throws DataExchangeException {
        return dataSyncConfigRepository.findById(tableName)
                .orElseThrow(() -> new DataExchangeException("No Table Found"));
    }

    private String prepareQuery(String query, boolean initialLoad, String timestamp, boolean useKeyPagination) {
        String operator;
        if (useKeyPagination) {
            operator = GREATER;//initialLoad ? GREATER_EQUAL : LESS;
        }
        else {
            operator = initialLoad ? LESS : GREATER_EQUAL;
        }
        return query.replaceAll(OPERATION, operator)
                .replaceAll(GENERIC_PARAM, "'" + timestamp + "'");
    }

    private Integer executeQueryForTotalRecords(String query, String sourceDb) throws DataExchangeException {
        try {
            if (sourceDb.equalsIgnoreCase(DB_RDB)) {
                return jdbcTemplate.queryForObject(query, Integer.class);
            } else if (sourceDb.equalsIgnoreCase(DB_SRTE)) {
                return srteJdbcTemplate.queryForObject(query, Integer.class);
            } else if (sourceDb.equalsIgnoreCase(DB_RDB_MODERN)) {
                return rdbModernJdbcTemplate.queryForObject(query, Integer.class);
            }
            else if (sourceDb.equalsIgnoreCase("NBS_ODSE")) {
                return odseJdbcTemplate.queryForObject(query, Integer.class);
            }
            else {
                throw new DataExchangeException("Database Not Supported: " + sourceDb);
            }
        } catch (Exception e) {
            throw new DataExchangeException("Error executing query: " + e.getMessage());
        }
    }

    public String getDataForDataSync(String tableName, String param, String startRow, String endRow,
                                     boolean initialLoad, boolean allowNull, boolean noPagination,
                                     boolean keyPagination) throws DataExchangeException {

        DataSyncConfig dataConfig = getConfigByTableName(tableName);

        DataSyncLog dataLog = new DataSyncLog();
        dataLog.setTableName(tableName);
        dataLog.setStatusSync("INPROGRESS");
        dataLog.setStartTime(getCurrentTimeStamp(tz));

        var log = dataSyncLogRepository.save(dataLog);
        AtomicInteger dataCountHolder = new AtomicInteger();

        try {
            Callable<String> callable = () -> {
                String baseQuery = preparePaginationQuery(dataConfig, param, startRow, endRow, initialLoad, allowNull, noPagination, keyPagination);

                List<Map<String, Object>> data = executeQueryForDataAsync(baseQuery, dataConfig.getSourceDb());

                dataCountHolder.set(data.size());


                return DataSimplification.dataToString(data);
                //return DataSimplification.dataCompressionAndEncodeV2(data);
            };

            return executeDataSyncQuery(callable, tableName, startRow, endRow, dataCountHolder, log);
        } catch (Exception exception) {
            exception.printStackTrace();
            log.setStatusSync("ERROR");
            log.setEndTime(getCurrentTimeStamp(tz));
            log.setErrorDesc(exception.getMessage());
            dataSyncLogRepository.save(log);
            throw new DataExchangeException(exception.getMessage());
        }

    }

    private String preparePaginationQuery(DataSyncConfig dataConfig, String param, String startRow,
                                          String endRow, boolean initialLoad, boolean allowNull, boolean noPagination, boolean keyPagination) {

        String baseQuery;

        if (allowNull && dataConfig.getQueryWithNullTimeStamp() != null && !dataConfig.getQueryWithNullTimeStamp().isEmpty()) {
            baseQuery = dataConfig.getQueryWithNullTimeStamp().replaceAll(";", "") ; //NOSONAR
        }
        else if (noPagination || dataConfig.getQueryWithPagination().isEmpty()) {
            baseQuery = dataConfig.getQuery().replaceAll(";", "") ; //NOSONAR
        }
        else
        {
            String operator;

            // ISOLATE these two on purpose, keyPagination will use KEY as condition while other will use timestmap
            // They similar for now but who know what change in future
            if (keyPagination) {
                operator = ">" ;
                baseQuery = dataConfig.getQueryWithPagination()
                        .replaceAll(GENERIC_PARAM, "'" + param + "'")
                        .replaceAll(START_ROW, startRow)
                        .replaceAll(END_ROW, endRow);
            }
            else {
                operator = initialLoad ? LESS : GREATER_EQUAL;
                baseQuery = dataConfig.getQueryWithPagination()
                        .replaceAll(GENERIC_PARAM, "'" + param + "'")
                        .replaceAll(START_ROW, startRow)
                        .replaceAll(END_ROW, endRow);
            }
            baseQuery = baseQuery.replaceAll(OPERATION, operator);

        }

        return baseQuery + ";";
    }

    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private static final Semaphore semaphore = new Semaphore(50); // Limits to 50 concurrent queries


    public List<Map<String, Object>> executeQueryForDataAsync(String query, String sourceDb) throws DataExchangeException {
        try {
            semaphore.acquire(); // Limit concurrent queries

            JdbcTemplate template = getJdbcTemplate(sourceDb);
            return CompletableFuture.supplyAsync(() -> {
                return queryWithStreaming(template, query);
            }, executor).get(); // Executes asynchronously
        } catch (Exception e) {
            throw new DataExchangeException("Error executing query on DB: " + e.getMessage());
        } finally {
            semaphore.release(); // Always release semaphore
        }
    }

    private JdbcTemplate getJdbcTemplate(String sourceDb) throws DataExchangeException {
        Map<String, JdbcTemplate> dbTemplateMap = Map.of(
                DB_SRTE, srteJdbcTemplate,
                DB_RDB, jdbcTemplate,
                DB_RDB_MODERN, rdbModernJdbcTemplate,
                "NBS_ODSE", odseJdbcTemplate
        );

        return Optional.ofNullable(dbTemplateMap.get(sourceDb))
                .orElseThrow(() -> new DataExchangeException("DB IS NOT SUPPORTED: " + sourceDb));
    }

    private List<Map<String, Object>> queryWithStreaming(JdbcTemplate template, String query) {
        List<Map<String, Object>> results = new ArrayList<>();
       // template.setQueryTimeout(30); // Prevent long-running queries

        template.query(query, rs -> {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
            results.add(row);
        });

        return results;
    }

    private List<Map<String, Object>> executeQueryForData(String query, String sourceDb) throws DataExchangeException {
        if (sourceDb.equalsIgnoreCase(DB_SRTE)) {
            return srteJdbcTemplate.queryForList(query);
        } else if (sourceDb.equalsIgnoreCase(DB_RDB)) {
            return jdbcTemplate.queryForList(query);
        } else if (sourceDb.equalsIgnoreCase(DB_RDB_MODERN)) {
            return rdbModernJdbcTemplate.queryForList(query);
        } else if (sourceDb.equalsIgnoreCase("NBS_ODSE")) {
            return odseJdbcTemplate.queryForList(query);
        }

        else {
            throw new DataExchangeException("DB IS NOT SUPPORTED: " + sourceDb);
        }
    }

    private String executeDataSyncQuery(Callable<String> callable, String tableName, String startRow, String endRow,
                                        AtomicInteger dataCountHolder, DataSyncLog log) throws DataExchangeException {
        try {
            var metricData = MetricCollector.measureExecutionTime(callable);
            var currentTime = getCurrentTimeStamp(tz);

            log.setTableName(tableName);
            log.setLastExecutedResultCount(dataCountHolder.get());
            log.setLastExecutedRunTime(metricData.getExecutionTime());
            log.setLastExecutedTimestamp(currentTime);
            log.setStartRow(startRow);
            log.setEndRow(endRow);
            log.setEndTime(getCurrentTimeStamp(tz));
            log.setStatusSync("SUCCESS");
            dataSyncLogRepository.save(log);

            return metricData.getResult();
        } catch (Exception e) {
            throw new DataExchangeException(e.getMessage());
        }
    }


    // DECODE TEST METHOD
    public String decodeAndDecompress(String base64EncodedData) throws DataExchangeException {
        return DataSimplification.decodeAndDecompress(base64EncodedData);
    }
}
