package gov.cdc.nnddataexchangeservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddataexchangeservice.configuration.TimestampAdapter;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.rdb.DataSyncConfigRepository;
import gov.cdc.nnddataexchangeservice.service.interfaces.IDataExchangeGenericService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static gov.cdc.nnddataexchangeservice.constant.DataSyncConstant.*;

@Service
public class DataExchangeGenericService implements IDataExchangeGenericService {
    private final DataSyncConfigRepository dataSyncConfigRepository;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcTemplate odseJdbcTemplate;
    private final Gson gson;

    public DataExchangeGenericService(DataSyncConfigRepository dataSyncConfigRepository,
                                      @Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate,
                                      @Qualifier("odseJdbcTemplate")  JdbcTemplate odseJdbcTemplate) {
        this.dataSyncConfigRepository = dataSyncConfigRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.odseJdbcTemplate = odseJdbcTemplate;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
                .serializeNulls()
                .create();
    }

    @SuppressWarnings("javasecurity:S3649")
    public String getGenericDataExchange(String tableName, String timeStamp, Integer limit) throws DataExchangeException {
        // Retrieve configuration based on table name
        var dataConfig = dataSyncConfigRepository.findById(tableName).orElseThrow(() -> new DataExchangeException("Selected Table Not Found"));

        if (timeStamp == null) {
            timeStamp = "";
        }
        try {
            // Execute the query and retrieve the dataset
            String baseQuery = (limit > 0 && dataConfig.getQueryWithLimit() != null && !dataConfig.getQueryWithLimit().isEmpty())
                    ? dataConfig.getQueryWithLimit()
                    : dataConfig.getQuery();

            String effectiveTimestamp = timeStamp.isEmpty() ? "'" + DEFAULT_TIME_STAMP +"'" : "'" + timeStamp + "'";
            String query = baseQuery.replace(TIME_STAMP_PARAM, effectiveTimestamp);

            if (baseQuery.contains(LIMIT_PARAM)) {
                query = query.replace(LIMIT_PARAM, limit.toString());
            }
            List<Map<String, Object>> data;

            if (dataConfig.getSourceDb().equalsIgnoreCase(DB_ODSE)) {
                data = odseJdbcTemplate.queryForList(query);
            } else {
                data = jdbcTemplate.queryForList(query);
            }


            // Serialize the data to JSON using Gson
            String jsonData = gson.toJson(data);

            // Compress the JSON data using GZIP and return the Base64 encoded result
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {

                gzipOutputStream.write(jsonData.getBytes());
                gzipOutputStream.finish();

                byte[] compressedData = byteArrayOutputStream.toByteArray();
                return Base64.getEncoder().encodeToString(compressedData);
            }

        } catch (IOException e) {
            // Catch IOException and throw DataExchangeException
            throw new DataExchangeException("Error during data compression or encoding");
        }
    }

    // DECODE TEST METHOD
    public String decodeAndDecompress(String base64EncodedData) {
        byte[] compressedData = Base64.getDecoder().decode(base64EncodedData);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BYTE_SIZE];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            String decompressedJson = byteArrayOutputStream.toString(UTF8);

            return decompressedJson;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
