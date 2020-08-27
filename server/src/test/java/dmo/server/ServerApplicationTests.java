package dmo.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@SpringJUnitConfig
@Testcontainers
@TestPropertySource(properties = {
	"logging.level.ROOT=INFO"
})
class ServerApplicationTests {

	@Container
	public static JdbcDatabaseContainer<?> database = new MariaDBContainer<>().withReuse(true);

	@DynamicPropertySource
	public static void updateConfig(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", database::getJdbcUrl);
		registry.add("spring.datasource.username", database::getUsername);
		registry.add("spring.datasource.password", database::getPassword);
	}

	@Test
	void contextLoads() {

	}
}
