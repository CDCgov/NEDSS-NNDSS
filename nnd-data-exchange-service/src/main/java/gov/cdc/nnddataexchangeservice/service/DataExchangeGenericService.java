package gov.cdc.nnddataexchangeservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddataexchangeservice.configuration.TimestampAdapter;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.rdb.DataSyncConfigRepository;
import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataSyncConfig;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import gov.cdc.nnddataexchangeservice.shared.DataSimplification;
import gov.cdc.nnddataexchangeservice.shared.MetricCollector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static gov.cdc.nnddataexchangeservice.constant.DataSyncConstant.*;
import static gov.cdc.nnddataexchangeservice.shared.TimestampHandler.getCurrentTimeStamp;

@Service
public class DataExchangeGenericService implements IDataExchangeGenericService {

    private final DataSyncConfigRepository dataSyncConfigRepository;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcTemplate srteJdbcTemplate;
    private final Gson gson;

    public DataExchangeGenericService(DataSyncConfigRepository dataSyncConfigRepository,
                                      @Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate,
                                      @Qualifier("srteJdbcTemplate")  JdbcTemplate srteJdbcTemplate) {
        this.dataSyncConfigRepository = dataSyncConfigRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.srteJdbcTemplate = srteJdbcTemplate;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
                .serializeNulls()
                .create();
    }

    @SuppressWarnings({"javasecurity:S3649", "java:S3776"})
    public String getGenericDataExchange(String tableName, String timeStamp, Integer limit,boolean initialLoad) throws DataExchangeException {
        // Retrieve configuration based on table name
        DataSyncConfig dataConfig = dataSyncConfigRepository.findById(tableName).orElse(new DataSyncConfig());

        if (timeStamp == null) {
            timeStamp = "";
        }

        AtomicInteger dataCountHolder = new AtomicInteger();
        String finalTimeStamp = timeStamp;

        Callable<String> callable = () -> {
            String dataCompressed = "";
            Integer dataCount = 0;
            List<Map<String, Object>> data = null;
            try {
                if (dataConfig.getTableName() == null) {
                    return DataSimplification.dataCompressionAndEncode("");
                }

                // Execute the query and retrieve the dataset
                String baseQuery = (limit > 0 && dataConfig.getQueryWithLimit() != null && !dataConfig.getQueryWithLimit().isEmpty())
                        ? dataConfig.getQueryWithLimit()
                        : dataConfig.getQuery();

                String effectiveTimestamp = finalTimeStamp.isEmpty() ? "'" + DEFAULT_TIME_STAMP + "'" : "'" + finalTimeStamp + "'";
                String query = baseQuery.replace(TIME_STAMP_PARAM, effectiveTimestamp);

                if (initialLoad) {
                    query = query.replaceAll(">=", "<"); // NOSONAR
                    if (dataConfig.getQueryWithNullTimeStamp() != null && !dataConfig.getQueryWithNullTimeStamp().isEmpty()) {
                        query = query.replaceAll(";", ""); // NOSONAR
                        var nullQuery = dataConfig.getQueryWithNullTimeStamp();
                        nullQuery = nullQuery.replaceAll(";", ""); // NOSONAR
                        query = nullQuery + " UNION " + query + ";";
                    }
                }

                if (baseQuery.contains(LIMIT_PARAM)) {
                    query = query.replace(LIMIT_PARAM, limit.toString());
                }


                if (dataConfig.getSourceDb().equalsIgnoreCase(DB_SRTE)) {
                    data = srteJdbcTemplate.queryForList(query);
                } else {
                    data = jdbcTemplate.queryForList(query);
                }

                // Serialize the data to JSON using Gson
                dataCompressed = DataSimplification.dataCompressionAndEncodeV2(gson, data);

                dataCount = data.size();

                // Store values for later use
                dataCountHolder.set(dataCount);

                return dataCompressed;
            } finally {
                // Explicitly nullify references to large objects to make them eligible for garbage collection
                if (data != null) {
                    data.clear();
                }
            }

        };

        try {
            var metricData = MetricCollector.measureExecutionTime(callable);
            var currentTime = getCurrentTimeStamp();

            dataSyncConfigRepository.updateDataSyncConfig(dataCountHolder.get(), metricData.getExecutionTime(), currentTime, tableName.toUpperCase());
            return metricData.getResult();
        } catch (Exception e) {
            throw new DataExchangeException(e.getMessage());
        }
    }

    private void initialLoadByBatchOLD(String inputQuery, DataSyncConfig dataConfig,  Integer limit, String effectiveTimestamp) {

        List<Map<String, Object>> data = null;
        String query = inputQuery.replaceAll(">=", "<"); // NOSONAR

        int totalRecords = 0;
        if (dataConfig.getQueryCount() != null && !dataConfig.getQueryCount().isEmpty()) {
            if (dataConfig.getSourceDb().equalsIgnoreCase(DB_SRTE)) {
                totalRecords = srteJdbcTemplate.queryForObject(dataConfig.getQueryCount(), new Object[]{effectiveTimestamp}, Integer.class);
            } else {
                totalRecords = jdbcTemplate.queryForObject(dataConfig.getQueryCount(), new Object[]{effectiveTimestamp}, Integer.class);
            }
        }

        int batchSize = limit;
        int totalPages = (int) Math.ceil((double) totalRecords / batchSize);



        if (dataConfig.getSourceDb().equalsIgnoreCase(DB_SRTE)) {
            for (int i = 0; i < totalPages; i++) {
                Integer startRow = i * batchSize + 1;
                Integer endRow = (i + 1) * batchSize;
                List<Map<String, Object>> batchData = new ArrayList<>();
                if (i == 0 && dataConfig.getQueryWithNullTimeStamp() != null && !dataConfig.getQueryWithNullTimeStamp().isEmpty()) {
                    String queryPagination = query;
                    queryPagination = queryPagination.replace(":startRow", startRow.toString()).replace(":endRow", endRow.toString());
                    var nullQuery = dataConfig.getQueryWithNullTimeStamp();
                    nullQuery = nullQuery.replaceAll(";", ""); // NOSONAR
                    String queryWithNull = nullQuery + " UNION " + queryPagination + ";";
                    batchData = srteJdbcTemplate.queryForList( queryWithNull); // UPDATE THIS TO WORK WITH BATCHES

                } else {
                    String queryContinualPagination = query;
                    queryContinualPagination = queryContinualPagination.replace(":startRow", startRow.toString()).replace(":endRow", endRow.toString());

                    // data = fetchProvidersByBatch(timestamp, startRow, endRow);
                    batchData = srteJdbcTemplate.queryForList(queryContinualPagination); // UPDATE THIS TO WORK WITH BATCHES

                }

                data.addAll(batchData);

            }
        }
        else {
            for (int i = 0; i < totalPages; i++) {
                Integer startRow = i * batchSize + 1;
                Integer endRow = (i + 1) * batchSize;
                List<Map<String, Object>> batchData = new ArrayList<>();
                if (i == 0 && dataConfig.getQueryWithNullTimeStamp() != null && !dataConfig.getQueryWithNullTimeStamp().isEmpty()) {
                    String queryPagination = query;
                    queryPagination = queryPagination.replace(":startRow", startRow.toString()).replace(":endRow", endRow.toString());
                    var nullQuery = dataConfig.getQueryWithNullTimeStamp();
                    nullQuery = nullQuery.replaceAll(";", ""); // NOSONAR
                    String queryWithNull = nullQuery + " UNION " + queryPagination + ";";
                    batchData = jdbcTemplate.queryForList(queryWithNull); // UPDATE THIS TO WORK WITH BATCHES
                } else {
                    String queryContinualPagination = query;
                    queryContinualPagination = queryContinualPagination.replace(":startRow", startRow.toString()).replace(":endRow", endRow.toString());
                    // data = fetchProvidersByBatch(timestamp, startRow, endRow);
                    batchData = jdbcTemplate.queryForList(queryContinualPagination); // UPDATE THIS TO WORK WITH BATCHES

                }

                data.addAll(batchData);
            }
        }

    }


    private void initialLoadByBatch(String inputQuery, DataSyncConfig dataConfig, Integer limit, String effectiveTimestamp) {
        List<Map<String, Object>> data = new ArrayList<>(); // Initialize the data list
        String query = inputQuery.replaceAll(">=", "<"); // NOSONAR to prevent query mutation

        int totalRecords = getTotalRecords(dataConfig, effectiveTimestamp);
        int batchSize = limit;
        int totalPages = (int) Math.ceil((double) totalRecords / batchSize);

        for (int i = 0; i < totalPages; i++) {
            int startRow = i * batchSize + 1;
            int endRow = (i + 1) * batchSize;
            List<Map<String, Object>> batchData = new ArrayList<>();

            if (i == 0 && isFirstBatchWithNullTimestamp(dataConfig))
            {
                batchData = executeFirstBatchWithNullTimestamp(query, dataConfig, startRow, endRow);
            }
            else
            {
                batchData = executeBatch(query, dataConfig, startRow, endRow);
            }

            data.addAll(batchData); // Append batch data to the main data list
        }
    }

    private int getTotalRecords(DataSyncConfig dataConfig, String effectiveTimestamp) {
        if (dataConfig.getQueryCount() != null && !dataConfig.getQueryCount().isEmpty()) {
            if (dataConfig.getSourceDb().equalsIgnoreCase(DB_SRTE)) {
                return srteJdbcTemplate.queryForObject(dataConfig.getQueryCount(), new Object[]{effectiveTimestamp}, Integer.class);
            } else {
                return jdbcTemplate.queryForObject(dataConfig.getQueryCount(), new Object[]{effectiveTimestamp}, Integer.class);
            }
        }
        return 0; // Default if no queryCount provided
    }

    private boolean isFirstBatchWithNullTimestamp(DataSyncConfig dataConfig) {
        return dataConfig.getQueryWithNullTimeStamp() != null && !dataConfig.getQueryWithNullTimeStamp().isEmpty();
    }

    private List<Map<String, Object>> executeFirstBatchWithNullTimestamp(String query, DataSyncConfig dataConfig, int startRow, int endRow) {
        String queryPagination = paginateQuery(query, startRow, endRow);
        String nullQuery = dataConfig.getQueryWithNullTimeStamp().replaceAll(";", ""); // NOSONAR to avoid mutation
        String queryWithNull = nullQuery + " UNION " + queryPagination;

        if (dataConfig.getSourceDb().equalsIgnoreCase(DB_SRTE)) {
            return srteJdbcTemplate.queryForList(queryWithNull); // Execute query for SRTE DB
        } else {
            return jdbcTemplate.queryForList(queryWithNull); // Execute query for default DB
        }
    }

    private List<Map<String, Object>> executeBatch(String query, DataSyncConfig dataConfig, int startRow, int endRow) {
        String paginatedQuery = paginateQuery(query, startRow, endRow);

        if (dataConfig.getSourceDb().equalsIgnoreCase(DB_SRTE)) {
            return srteJdbcTemplate.queryForList(paginatedQuery); // Execute query for SRTE DB
        } else {
            return jdbcTemplate.queryForList(paginatedQuery); // Execute query for default DB
        }
    }

    private String paginateQuery(String query, int startRow, int endRow) {
        return query.replace(":startRow", String.valueOf(startRow))
                .replace(":endRow", String.valueOf(endRow));
    }


    // DECODE TEST METHOD
    public String decodeAndDecompress(String base64EncodedData) throws DataExchangeException {
        return DataSimplification.decodeAndDecompress(base64EncodedData);
    }
}
