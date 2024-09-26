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

    public Integer getTotalRecord(String tableName, boolean initialLoad, String timestamp) throws DataExchangeException {
        DataSyncConfig dataConfig = getConfigByTableName(tableName);

        String query = prepareQuery(dataConfig.getQueryCount(), initialLoad, timestamp);

        return executeQueryForTotalRecords(query, dataConfig.getSourceDb());
    }

    private DataSyncConfig getConfigByTableName(String tableName) throws DataExchangeException {
        return dataSyncConfigRepository.findById(tableName)
                .orElseThrow(() -> new DataExchangeException("No Table Found"));
    }

    private String prepareQuery(String query, boolean initialLoad, String timestamp) {
        String operator = initialLoad ? LESS : GREATER_EQUAL;
        return query.replaceAll(OPERATION, operator)
                .replaceAll(TIME_STAMP_PARAM, timestamp);
    }

    private Integer executeQueryForTotalRecords(String query, String sourceDb) throws DataExchangeException {
        try {
            if (sourceDb.equalsIgnoreCase(DB_RDB)) {
                return jdbcTemplate.queryForObject(query, Integer.class);
            } else if (sourceDb.equalsIgnoreCase(DB_SRTE)) {
                return srteJdbcTemplate.queryForObject(query, Integer.class);
            } else if (sourceDb.equalsIgnoreCase(DB_RDB_MODERN)) {
                throw new DataExchangeException("TO BE IMPLEMENTED");
            } else {
                throw new DataExchangeException("Database Not Supported: " + sourceDb);
            }
        } catch (Exception e) {
            throw new DataExchangeException("Error executing query: " + e.getMessage());
        }
    }

    public String getDataForDataSync(String tableName, String timeStamp, String startRow, String endRow,
                                     boolean initialLoad, boolean allowNull) throws DataExchangeException {

        DataSyncConfig dataConfig = getConfigByTableName(tableName);

        AtomicInteger dataCountHolder = new AtomicInteger();

        Callable<String> callable = () -> {
            String baseQuery = preparePaginationQuery(dataConfig, timeStamp, startRow, endRow, initialLoad, allowNull);

            List<Map<String, Object>> data = executeQueryForData(baseQuery, dataConfig.getSourceDb());

            dataCountHolder.set(data.size());

            return DataSimplification.dataCompressionAndEncodeV2(gson, data);
        };

        return executeDataSyncQuery(callable, tableName, startRow, endRow, dataCountHolder);
    }

    private String preparePaginationQuery(DataSyncConfig dataConfig, String timeStamp, String startRow,
                                          String endRow, boolean initialLoad, boolean allowNull) {

        String baseQuery = dataConfig.getQueryWithPagination()
                .replaceAll(TIME_STAMP_PARAM, timeStamp)
                .replaceAll(START_ROW, startRow)
                .replaceAll(END_ROW, endRow);

        String operator = initialLoad ? LESS : GREATER_EQUAL;
        baseQuery = baseQuery.replaceAll(OPERATION, operator);

        if (initialLoad && allowNull && dataConfig.getQueryWithNullTimeStamp() != null && !dataConfig.getQueryWithNullTimeStamp().isEmpty()) {
            baseQuery = dataConfig.getQueryWithNullTimeStamp().replaceAll(";", "") + " UNION " + baseQuery;
        }

        return baseQuery + ";";
    }

    private List<Map<String, Object>> executeQueryForData(String query, String sourceDb) throws DataExchangeException {
        if (sourceDb.equalsIgnoreCase(DB_SRTE)) {
            return srteJdbcTemplate.queryForList(query);
        } else if (sourceDb.equalsIgnoreCase(DB_RDB)) {
            return jdbcTemplate.queryForList(query);
        } else if (sourceDb.equalsIgnoreCase(DB_RDB_MODERN)) {
            throw new DataExchangeException("TO BE IMPLEMENTED");
        } else {
            throw new DataExchangeException("DB IS NOT SUPPORTED: " + sourceDb);
        }
    }

    private String executeDataSyncQuery(Callable<String> callable, String tableName, String startRow, String endRow,
                                        AtomicInteger dataCountHolder) throws DataExchangeException {
        try {
            var metricData = MetricCollector.measureExecutionTime(callable);
            var currentTime = getCurrentTimeStamp();

            dataSyncConfigRepository.updateDataSyncConfig(dataCountHolder.get(), metricData.getExecutionTime(), currentTime,
                    tableName.toUpperCase(), startRow, endRow);

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
