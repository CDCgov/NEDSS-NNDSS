package gov.cdc.nnddatapollservice.service.rdb.service;

import gov.cdc.nnddatapollservice.exception.DataPollException;
import gov.cdc.nnddatapollservice.service.rdb.service.interfaces.ILocalDirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static gov.cdc.nnddatapollservice.constant.ConstantValue.LOG_SUCCESS;

@Service
public class LocalDirectoryService implements ILocalDirectoryService {
    private static Logger logger = LoggerFactory.getLogger(LocalDirectoryService.class);

    @Value("${local_dir.path}")
    private String baseDirectory;

    public LocalDirectoryService() {
    }

    public String persistToLocalDirectory(String records, String fileName, Timestamp persistingTimestamp, boolean initialLoad) throws DataPollException {
        String log = LOG_SUCCESS;
        try {
            if (records.equalsIgnoreCase("[]") || records.isEmpty()) {
                throw new DataPollException("No data to persist for table " + fileName);
            }

            // Format the timestamp into yyyyMMddHHmmss format
            String formattedTimestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(persistingTimestamp);

            // Construct the file path based on initialLoad flag
            String localFilePath;
            if (initialLoad) {
                localFilePath = String.format("%s/%s/initial_load_%s_%s.json", baseDirectory, fileName, fileName, formattedTimestamp);
            } else {
                localFilePath = String.format("%s/%s/%s_%s.json", baseDirectory, fileName, fileName, formattedTimestamp);
            }

            // Ensure that the directories exist or create them
            Path directoryPath = Paths.get(localFilePath).getParent();
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Write the records in chunks to the file
            try (InputStream inputStream = new ByteArrayInputStream(records.getBytes(StandardCharsets.UTF_8));
                 BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilePath))) {

                byte[] buffer = new byte[5 * 1024 * 1024]; // 5MB buffer size
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            logger.info("Successfully persisted to local directory: " + localFilePath);
        } catch (IOException e) {
            logger.error("Error persisting data to local directory: " + e.getMessage());
            log = e.getMessage();
        }
        return log;
    }
}
