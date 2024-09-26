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
        DataSyncConfig dataConfig = dataSyncConfigRepository.findById(tableName)
                .orElseThrow(() -> new DataExchangeException("No Table Found"));

        if (dataConfig.getTableName() != null && !dataConfig.getTableName().isEmpty()) {
            String query = dataConfig.getQueryCount();

            // Set the appropriate comparison operator
            if (initialLoad) {
                query = query.replaceAll(OPERATION, LESS);
            } else {
                query = query.replaceAll(OPERATION, GREATER_EQUAL);
            }

            // Replace the timestamp placeholder
            query = query.replaceAll(TIME_STAMP_PARAM, timestamp);

            // Execute the query based on the source database
            try {
                if (dataConfig.getSourceDb().equalsIgnoreCase(DB_RDB)) {
                    return jdbcTemplate.queryForObject(query, Integer.class);
                } else if (dataConfig.getSourceDb().equalsIgnoreCase(DB_SRTE)) {
                    return srteJdbcTemplate.queryForObject(query, Integer.class);
                } else if (dataConfig.getSourceDb().equalsIgnoreCase(DB_RDB_MODERN)) {
                    throw new DataExchangeException("TO BE IMPLEMENTED");
                } else {
                    throw new DataExchangeException("Database Not Supported: " + dataConfig.getSourceDb());
                }
            } catch (Exception e) {
                throw new DataExchangeException("Error executing query: " + e.getMessage());
            }
        } else {
            throw new DataExchangeException("No Table Found");
        }
    }
    public String getDataForDataSync(String tableName, String timeStamp, String startRow, String endRow,
                                   boolean initialLoad, boolean allowNull) throws DataExchangeException {
        // Retrieve configuration based on table name
        DataSyncConfig dataConfig = dataSyncConfigRepository.findById(tableName).orElse(new DataSyncConfig());


        AtomicInteger dataCountHolder = new AtomicInteger();

        Callable<String> callable = () -> {
            String dataCompressed = "";
            Integer dataCount = 0;
            List<Map<String, Object>> data = null;
            try {
                if (dataConfig.getTableName() == null) {
                    return DataSimplification.dataCompressionAndEncode("");
                }

                // Execute the query and retrieve the dataset
                String baseQuery =  dataConfig.getQueryWithPagination();
                baseQuery = baseQuery.replaceAll(TIME_STAMP_PARAM, timeStamp);
                baseQuery = baseQuery.replaceAll(START_ROW, startRow);
                baseQuery = baseQuery.replaceAll(END_ROW, endRow);


                if (initialLoad) {
                    baseQuery = baseQuery.replaceAll(OPERATION, LESS);
                    if (allowNull && dataConfig.getQueryWithNullTimeStamp() != null && !dataConfig.getQueryWithNullTimeStamp().isEmpty()) {
                        baseQuery = baseQuery.replaceAll(";", ""); // NOSONAR
                        String nullQuery = dataConfig.getQueryWithNullTimeStamp();
                        nullQuery = nullQuery.replaceAll(";", ""); // NOSONAR
                        baseQuery = nullQuery + " UNION " + baseQuery + ";";
                    }
                }
                else {
                    baseQuery = baseQuery.replaceAll(OPERATION, GREATER_EQUAL);
                }



                if (dataConfig.getSourceDb().equalsIgnoreCase(DB_SRTE))
                {
                    data = srteJdbcTemplate.queryForList(baseQuery);
                }
                else if (dataConfig.getSourceDb().equalsIgnoreCase(DB_RDB))
                {
                    data = jdbcTemplate.queryForList(baseQuery);
                }
                else if (dataConfig.getSourceDb().equalsIgnoreCase(DB_RDB_MODERN))
                {
                    throw new DataExchangeException("TO BE IMPLEMENTED");
                }
                else {
                    throw new DataExchangeException("DB IS NOT SUPPORTED: " + dataConfig.getSourceDb());
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
            dataSyncConfigRepository.updateDataSyncConfig(dataCountHolder.get(), metricData.getExecutionTime(), currentTime, tableName.toUpperCase(),
                    startRow, endRow);
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
