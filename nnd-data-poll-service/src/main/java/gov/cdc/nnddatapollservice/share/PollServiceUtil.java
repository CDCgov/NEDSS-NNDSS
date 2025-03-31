package gov.cdc.nnddatapollservice.share;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.cdc.nnddatapollservice.service.model.ApiResponseModel;
import gov.cdc.nnddatapollservice.service.model.LogResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.ERROR;
import static gov.cdc.nnddatapollservice.constant.ConstantValue.SUCCESS;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

public class PollServiceUtil {
    private static Logger logger = LoggerFactory.getLogger(PollServiceUtil.class);
    private static final String TIMESTAMP_FOR_FILE_FORMAT = "yyyyMMddHHmmss";

    private PollServiceUtil() {
        throw new IllegalStateException("PollServiceUtil cannot be instantiated");
    }

    public static LogResponseModel writeJsonToFile(String localfilePath, String dbSource, String tableName,
                                                   Timestamp timeStamp, String jsonData,
                                                   ApiResponseModel<?> apiResponseModel) {
        LogResponseModel logResponseModel = new LogResponseModel(apiResponseModel);
        try {
            if (jsonData != null && !jsonData.equalsIgnoreCase("[]") && !jsonData.isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FOR_FILE_FORMAT);
                String updatedTime = formatter.format(timeStamp);
                Path dirPath
                        = Paths.get(localfilePath, dbSource, tableName);
                Path filePath = Paths.get(dirPath.toString(), tableName.toLowerCase() + "_" + updatedTime + ".json");
                Files.createDirectories(dirPath);
                Files.writeString(filePath, jsonData, StandardOpenOption.CREATE);
                logger.info("Successfully wrote json file to {}", filePath);
            }
        } catch (Exception e) {
            logger.error("Error writing to file", e);
            logResponseModel.setLog(e.getMessage());
            logResponseModel.setStackTrace(getStackTraceAsString(e));
            logResponseModel.setStatus(ERROR);
        }

        logResponseModel.setStatus(SUCCESS);
        return logResponseModel;
    }

    @SuppressWarnings("java:S3776")
    public static List<Map<String, Object>> jsonToListOfMap(String jsonData) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (jsonData != null && !jsonData.isEmpty()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            Type resultType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> tempList = gson.fromJson(jsonData, resultType);

            // Define possible datetime formats (modify if needed)
            List<DateTimeFormatter> formatters = Arrays.asList(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"), // ISO format
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            );

            for (Map<String, Object> tempRecord : tempList) {
                Map<String, Object> convertedRecord = new HashMap<>(tempRecord);

                for (Map.Entry<String, Object> entry : tempRecord.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        String value = (String) entry.getValue();

                        // Attempt to parse if it looks like a date/time string
                        for (DateTimeFormatter formatter : formatters) {
                            try {
                                LocalDateTime parsedDate = LocalDateTime.parse(value, formatter);
                                convertedRecord.put(entry.getKey(), parsedDate);
                                break; // Stop checking once it successfully converts
                            } catch (Exception e) {
                                // Ignore and continue checking formats
                            }
                        }
                    }
                }
                list.add(convertedRecord);
            }
        }
        return list;
    }

}