package dmo.server;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;

import dmo.server.ServerApplicationTests.Initializer;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = { Initializer.class })
class ServerApplicationTests {

	@Test
	void contextLoads() {

	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
				JdbcDatabaseContainer<?> database = new MariaDBContainer<>();
				
				database.start();

	            TestPropertyValues.of(
	              "spring.datasource.url=" + database.getJdbcUrl(),
	              "spring.datasource.username=" + database.getUsername(),
	              "spring.datasource.password=" + database.getPassword()
	            ).applyTo(configurableApplicationContext);
	        }
	}

}
