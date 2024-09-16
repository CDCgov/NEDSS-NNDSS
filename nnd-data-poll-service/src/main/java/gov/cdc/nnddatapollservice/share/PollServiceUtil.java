package gov.cdc.nnddatapollservice.share;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class PollServiceUtil {

    private static final String TIMESTAMP_FOR_FILE_FORMAT = "yyyy-MM-dd.HH.mm.ss";
    private PollServiceUtil() {
        throw new IllegalStateException("It cannot be instantiated");
    }
    public static void writeJsonToFile(String localfilePath, String dbSource, String tableName, Timestamp timeStamp, String jsonData) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FOR_FILE_FORMAT);
            String updatedTime = formatter.format(timeStamp);
            Path dirPath
                    = Paths.get(localfilePath, dbSource, tableName);
            Path filePath
                    = Paths.get(dirPath.toString(), tableName.toLowerCase() + "_" + updatedTime + ".json");
            Files.createDirectories(dirPath);
            Files.writeString(filePath, jsonData, StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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