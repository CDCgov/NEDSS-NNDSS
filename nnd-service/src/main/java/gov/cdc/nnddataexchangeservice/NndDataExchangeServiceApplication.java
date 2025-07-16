package gov.cdc.nnddataexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NndDataExchangeServiceApplication  {
    private static final Logger logger = LoggerFactory.getLogger(NndDataExchangeServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NndDataExchangeServiceApplication.class, args);
    }


}
