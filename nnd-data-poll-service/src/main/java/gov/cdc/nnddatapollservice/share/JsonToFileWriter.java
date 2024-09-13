package gov.cdc.nnddatapollservice.share;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class JsonToFileWriter {

    private static final String TIMESTAMP_FOR_FILE_FORMAT = "yyyy-MM-dd.HH.mm.ss";

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
}