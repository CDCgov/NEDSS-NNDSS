package gov.cdc.nnddataexchangeservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddataexchangeservice.configuration.TimestampAdapter;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import gov.cdc.nnddataexchangeservice.repository.rdb.DataExchangeConfigRepository;
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

@Service
public class DataExchangeGenericService implements IDataExchangeGenericService {
    private final DataExchangeConfigRepository dataExchangeConfigRepository;
    private final JdbcTemplate jdbcTemplate;
    private final Gson gson;

    public DataExchangeGenericService(DataExchangeConfigRepository dataExchangeConfigRepository,
                                      @Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.dataExchangeConfigRepository = dataExchangeConfigRepository;
        this.jdbcTemplate = jdbcTemplate;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
                .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
                .create();
    }

    @SuppressWarnings("javasecurity:S3649")
    public String getGenericDataExchange(String tableName, String timeStamp, Integer limit) throws DataExchangeException {
        // Retrieve configuration based on table name
        var dataConfig = dataExchangeConfigRepository.findById(tableName).orElseThrow(() -> new DataExchangeException("Selected Table Not Found"));

        if (timeStamp == null) {
            timeStamp = "";
        }
        try {
            // Execute the query and retrieve the dataset
            String baseQuery = (limit > 0 && dataConfig.getQueryWithLimit() != null && !dataConfig.getQueryWithLimit().isEmpty())
                    ? dataConfig.getQueryWithLimit()
                    : dataConfig.getQuery();

            String effectiveTimestamp = timeStamp.isEmpty() ? "'1753-01-01'" : "'" + timeStamp + "'";
            String query = baseQuery.replace(":timestamp", effectiveTimestamp);

            if (baseQuery.contains(":limit")) {
                query = query.replace(":limit", limit.toString());
            }

            List<Map<String, Object>> data = jdbcTemplate.queryForList(query);


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

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            String decompressedJson = byteArrayOutputStream.toString("UTF-8");

            return decompressedJson;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
