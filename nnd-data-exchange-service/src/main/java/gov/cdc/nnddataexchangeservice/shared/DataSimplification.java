package gov.cdc.nnddataexchangeservice.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;
import gov.cdc.nnddataexchangeservice.configuration.TimestampAdapter;
import gov.cdc.nnddataexchangeservice.exception.DataExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static gov.cdc.nnddataexchangeservice.constant.DataSyncConstant.BYTE_SIZE;

public class DataSimplification {
    private DataSimplification() {
    }

    public static String dataCompressionAndEncodeV2(List<?> data) throws IOException {
        Logger logger = LoggerFactory.getLogger("DataCompressionUtil"); // Adjust logger name for API context
        try {
            if (data == null) {
                logger.warn("Input data is null, returning empty Base64 string");
                return Base64.getEncoder().encodeToString(new byte[0]);
            }

            logger.debug("API: Compressing and encoding list of size: {}", data.size());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampSerializer())
                    .registerTypeAdapter(Timestamp.class, TimestampAdapter.getTimestampDeserializer())
                    .serializeNulls()
                    .create();

            // First attempt: Serialize without sanitization
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024 * 1024); // 1MB initial buffer
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(gzipOutputStream, StandardCharsets.UTF_8);
                 JsonWriter jsonWriter = new JsonWriter(outputStreamWriter)) {

                try {
                    logger.debug("API: Attempting to serialize data without sanitization.");
                    gson.toJson(data, List.class, jsonWriter);
                    jsonWriter.flush();
                    logger.debug("API: Successfully serialized data without sanitization.");
                } catch (JsonIOException | ArrayIndexOutOfBoundsException e) {
                    // If serialization fails, sanitize the data and retry
                    logger.warn("API: Initial serialization failed: {}. Retrying with sanitized data.", e.getMessage(), e);

                    // Sanitize the data
                    List<?> sanitizedData = sanitizeData(data, logger);
                    if (sanitizedData.isEmpty() && !data.isEmpty()) {
                        logger.warn("All data was sanitized out; returning empty result");
                        return Base64.getEncoder().encodeToString(new byte[0]);
                    }

                    // Reset the output stream for the retry
                    byteArrayOutputStream.reset();
                    try (GZIPOutputStream retryGzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                         OutputStreamWriter retryOutputStreamWriter = new OutputStreamWriter(retryGzipOutputStream, StandardCharsets.UTF_8);
                         JsonWriter retryJsonWriter = new JsonWriter(retryOutputStreamWriter)) {

                        // Retry serialization with sanitized data
                        logger.debug("API: Retrying serialization with sanitized data. Size: {}", sanitizedData.size());
                        try {
                            gson.toJson(sanitizedData, List.class, retryJsonWriter);
                            retryJsonWriter.flush();
                            logger.info("API: Successfully serialized data after sanitization.");
                        } catch (JsonIOException | ArrayIndexOutOfBoundsException e2) {
                            logger.error("API: Serialization failed even after sanitization. Size: {}, Sample: {}",
                                    sanitizedData.size(), sanitizedData.subList(0, Math.min(5, sanitizedData.size())));
                            throw new IOException("Serialization failed even after sanitization", e2);
                        }
                    }
                }
            }

            byte[] compressedData = byteArrayOutputStream.toByteArray();
            String result = Base64.getEncoder().encodeToString(compressedData);
            logger.debug("API: Successfully encoded {} bytes to Base64 string of length {}", compressedData.length, result.length());
            return result;
        } catch (Exception e) {
            logger.error("API: Error in dataCompressionAndEncodeV2: {}", e.getMessage(), e);
            throw new IOException("Failed to compress and encode data", e);
        }
    }
    /**
     * Sanitizes the input data to prevent serialization issues.
     * @param data Input list to sanitize
     * @param logger Logger for diagnostics
     * @return Sanitized list
     */
    private static List<?> sanitizeData(List<?> data, Logger logger) {
        // Check for excessively large data
        if (data.size() > 10000) {
            logger.warn("API: Data size {} exceeds recommended limit of 10,000. Consider paginating.", data.size());
            // Optionally truncate or handle pagination
            // data = data.subList(0, 10000);
        }

        List<Object> sanitized = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            Object item = data.get(i);
            try {
                if (item == null) {
                    sanitized.add(null); // Gson handles nulls with serializeNulls()
                } else if (item.getClass().isArray()) {
                    // Convert arrays to lists to avoid potential Gson array issues
                    Object[] array = (Object[]) item;
                    List<Object> sanitizedArray = new ArrayList<>(array.length);
                    for (Object element : array) {
                        sanitizedArray.add(sanitizeValue(element, logger, i));
                    }
                    sanitized.add(sanitizedArray);
                    logger.trace("API: Converted array at index {} to list", i);
                } else if (item instanceof Collection) {
                    // Ensure collections are not malformed
                    Collection<?> collection = (Collection<?>) item;
                    List<Object> sanitizedCollection = new ArrayList<>(collection.size());
                    for (Object element : collection) {
                        sanitizedCollection.add(sanitizeValue(element, logger, i));
                    }
                    sanitized.add(sanitizedCollection);
                } else if (item instanceof Map) {
                    // Ensure maps are not malformed and sanitize their values
                    Map<?, ?> map = (Map<?, ?>) item;
                    Map<Object, Object> sanitizedMap = new HashMap<>(map.size());
                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        Object key = sanitizeValue(entry.getKey(), logger, i);
                        Object value = sanitizeValue(entry.getValue(), logger, i);
                        sanitizedMap.put(key, value);
                    }
                    sanitized.add(sanitizedMap);
                } else {
                    sanitized.add(sanitizeValue(item, logger, i));
                }
            } catch (Exception e) {
                logger.warn("API: Skipping malformed item at index {}: {}", i, e.getMessage(), e);
            }
        }
        logger.info("API: Sanitized data size: {}", sanitized.size());
        return sanitized;
    }

    // Helper method to sanitize individual values
    private static Object sanitizeValue(Object value, Logger logger, int parentIndex) {
        if (value == null) {
            return null;
        }

        // Handle Timestamp objects
        if (value instanceof Timestamp) {
            return value.toString(); // Convert Timestamp to String to avoid Gson issues
        }

        // Handle Strings (remove or replace non-ASCII characters)
        if (value instanceof String) {
            String str = (String) value;
            // Check for non-ASCII characters
            if (str.matches(".*[^\\x00-\\x7F].*")) {
                logger.warn("API: Non-ASCII characters found in string at parent index {}: {}. Replacing with ASCII equivalent.", parentIndex, str);
                // Replace non-ASCII characters (you can customize this logic)
                str = str.replaceAll("[^\\x00-\\x7F]", "?");
            }
            // Check for control characters (e.g., \n, \t) that might break JSON
            if (str.matches(".*[\\p{Cntrl}].*")) {
                logger.warn("API: Control characters found in string at parent index {}: {}. Removing control characters.", parentIndex, str);
                str = str.replaceAll("[\\p{Cntrl}]", "");
            }
            return str;
        }

        // Handle nested maps or collections recursively
        if (value instanceof Map) {
            Map<Object, Object> sanitizedMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                Object key = sanitizeValue(entry.getKey(), logger, parentIndex);
                Object valueSanitized = sanitizeValue(entry.getValue(), logger, parentIndex);
                sanitizedMap.put(key, valueSanitized);
            }
            return sanitizedMap;
        }

        if (value instanceof Collection) {
            List<Object> sanitizedCollection = new ArrayList<>();
            for (Object element : (Collection<?>) value) {
                sanitizedCollection.add(sanitizeValue(element, logger, parentIndex));
            }
            return sanitizedCollection;
        }

        // Return primitives, numbers, or other types as-is
        return value;
    }

    public static String dataCompressionAndEncode(String jsonData) throws IOException {
        // Compress the JSON data using GZIP and return the Base64 encoded result
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream))
        {

            gzipOutputStream.write(jsonData.getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.finish();

            byte[] compressedData = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(compressedData);
        }
    }
    // DECODE TEST METHOD
    public static String decodeAndDecompress(String base64EncodedData) throws DataExchangeException {
        byte[] compressedData = Base64.getDecoder().decode(base64EncodedData);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BYTE_SIZE];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DataExchangeException(e.getMessage());
        }
    }
}
