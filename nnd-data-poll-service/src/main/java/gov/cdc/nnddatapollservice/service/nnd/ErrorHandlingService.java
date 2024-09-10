package gov.cdc.nnddatapollservice.service.nnd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.cdc.nnddatapollservice.service.nnd.interfaces.IErrorHandlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
public class ErrorHandlingService implements IErrorHandlingService {
    private static Logger logger = LoggerFactory.getLogger(ErrorHandlingService.class);

    public <T> void dumpBatchToFile(List<T> batch, String fileName, String fileLocation) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); ;

        File file = new File(fileLocation, fileName);
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(batch, writer);
        } catch (IOException e) {
            // Handle the exception if the file writing fails
            logger.error("Failed to write batch to file: {}", e.getMessage());
        }
    }
}
