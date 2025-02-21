package gov.cdc.nnddatapollservice.share;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.util.List;
import java.util.Map;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.ERROR;
import static gov.cdc.nnddatapollservice.constant.ConstantValue.SUCCESS;
import static gov.cdc.nnddatapollservice.share.StringUtil.getStackTraceAsString;

public class PollServiceUtil {
    private static Logger logger = LoggerFactory.getLogger(PollServiceUtil.class);
    private static final String TIMESTAMP_FOR_FILE_FORMAT = "yyyyMMddHHmmss";

    private PollServiceUtil() {
        throw new IllegalStateException("PollServiceUtil cannot be instantiated");
    }

    public static LogResponseModel writeJsonToFile(String localfilePath, String dbSource, String tableName, Timestamp timeStamp, String jsonData) {
        LogResponseModel logResponseModel = new LogResponseModel();
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

    public static List<Map<String, Object>> jsonToListOfMap(String jsonData) {
        List<Map<String, Object>> list = null;
        if (jsonData != null && !jsonData.isEmpty()) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            Type resultType = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            list = gson.fromJson(jsonData, resultType);
        }
        return list;
    }
}