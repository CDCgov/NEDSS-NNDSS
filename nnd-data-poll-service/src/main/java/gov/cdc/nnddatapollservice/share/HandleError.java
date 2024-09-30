package gov.cdc.nnddatapollservice.share;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HandleError {
    private static Logger logger = LoggerFactory.getLogger(HandleError.class);

    public static void writeRecordToFileTypedObject(Gson gson, Object record, String fileName, String directory) {
        try {
            Path dirPath = Paths.get(directory);
            Path filePath = Paths.get(dirPath.toString(), "failed_records_" + fileName  + ".json");
            Files.createDirectories(dirPath);
            String jsonData = gson.toJson(record);
            Files.writeString(filePath, jsonData + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            logger.info("Record written to file for later reprocessing: {}", gson.toJson(record));
        } catch (Exception e) {
            logger.error("Failed to write record to file: {}", e.getMessage());
        }

    }

    public static void writeRecordToFile(Gson gson, Map<String, Object> record, String fileName, String directory) {
        try {
            Path dirPath = Paths.get(directory);
            Path filePath = Paths.get(dirPath.toString(), "failed_records_" + fileName  + ".json");
            Files.createDirectories(dirPath);
            String jsonData = gson.toJson(record);
            Files.writeString(filePath, jsonData + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            logger.info("Record written to file for later reprocessing: {}", gson.toJson(record));
        } catch (Exception e) {
            logger.error("Failed to write record to file: {}", e.getMessage());
        }

    }

    public static List<Map<String, Object>> readFailedRecordsFromFile(Gson gson, String fileName, String directory) {
        List<Map<String, Object>> failedRecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(directory + "failed_records_" + fileName + ".json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Map<String, Object> record = gson.fromJson(line, Map.class);
                failedRecords.add(record);
            }
        } catch (IOException e) {
            logger.error("Failed to read records from file: {}", e.getMessage());
        }
        return failedRecords;
    }

    // Method to read all records from JSON files in a directory that match a naming pattern
    public static List<Map<String, Object>> readFailedRecordsFromDirectory(Gson gson, String directoryPath, String filePattern) {
        List<Map<String, Object>> failedRecords = new ArrayList<>();
        File folder = new File(directoryPath);
        if (!folder.exists() || !folder.isDirectory()) {
            logger.error("Directory does not exist or is not a folder: {}", directoryPath);
            return failedRecords;
        }

        // Define the pattern for file names (e.g., "A_1.json", "B_2.json", etc.)
        Pattern pattern = Pattern.compile(filePattern);

        File[] files = folder.listFiles((dir, name) -> pattern.matcher(name).matches() && name.endsWith(".json"));

        if (files == null || files.length == 0) {
            logger.info("No matching files found in directory: {}", directoryPath);
            return failedRecords;
        }

        for (File file : files) {
            logger.info("Reading file: {}", file.getName());
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Map<String, Object> record = gson.fromJson(line, Map.class);
                    failedRecords.add(record); // Add each record to the list
                }
            } catch (IOException e) {
                logger.error("Failed to read records from file: {}", file.getName(), e);
            }
        }

        return failedRecords;
    }
}
