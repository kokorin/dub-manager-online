package dmo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJdbcRepositories
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
