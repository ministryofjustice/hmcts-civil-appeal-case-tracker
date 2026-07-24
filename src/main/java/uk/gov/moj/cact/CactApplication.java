package uk.gov.moj.cact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CactApplication {

	public static void main(String[] args) {
		SpringApplication.run(CactApplication.class, args);
	}

}
