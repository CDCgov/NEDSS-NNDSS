package gov.cdc.nnddatapollservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NndDataPollServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NndDataPollServiceApplication.class, args);
    }

}
