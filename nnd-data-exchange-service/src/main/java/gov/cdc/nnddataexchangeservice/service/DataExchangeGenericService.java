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

import java.io.StringWriter;
import java.sql.Timestamp;
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
        var dataConfig = dataSyncConfigRepository.findById(tableName).orElse(new DataSyncConfig());

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

                // Encourage garbage collection
                System.gc();  // Optional, use cautiously as it can affect performance
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



    // DECODE TEST METHOD
    public String decodeAndDecompress(String base64EncodedData) throws DataExchangeException {
        return DataSimplification.decodeAndDecompress(base64EncodedData);
    }
}
