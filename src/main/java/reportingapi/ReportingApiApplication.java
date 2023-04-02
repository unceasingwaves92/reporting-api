package reportingapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class ReportingApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ReportingApiApplication.class, args);
		log.info("Reporting application has been started");
	}

}
