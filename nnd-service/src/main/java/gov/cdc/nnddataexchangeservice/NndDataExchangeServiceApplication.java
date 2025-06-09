package gov.cdc.nnddataexchangeservice;

import gov.cdc.nnddataexchangeservice.data_generator.DataGeneratorApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class NndDataExchangeServiceApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(NndDataExchangeServiceApplication.class);

    private final DataGeneratorApp dataGeneratorApp;
    private final Environment environment;

    @Autowired
    public NndDataExchangeServiceApplication(DataGeneratorApp dataGeneratorApp, Environment environment) {
        this.dataGeneratorApp = dataGeneratorApp;
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(NndDataExchangeServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Check if the 'data-generation' profile is active
        // VMOption = -Dspring.profiles.active=data-generation
        String[] activeProfiles = environment.getActiveProfiles();

        if (activeProfiles.length > 0 && "data-generation".equals(activeProfiles[0])) {
            dataGeneratorApp.run();
            logger.info("Data generation complete.");
        } else {
            logger.info("Default application behavior: Running NndDataExchangeServiceApplication.");
        }
    }
}
