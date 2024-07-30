package gov.cdc.nndmessageprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NndMessageProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NndMessageProcessorApplication.class, args);
	}

}
