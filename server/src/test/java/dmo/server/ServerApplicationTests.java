package dmo.server;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;
import java.nio.charset.Charset;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
@Testcontainers
@TestPropertySource(properties = {
	"logging.level.ROOT=INFO"
})
class ServerApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Container
	public static JdbcDatabaseContainer<?> database = new MariaDBContainer<>()
			.withReuse(true)
			.withConfigurationOverride("/dmo/server/maria_conf_d");

	@DynamicPropertySource
	public static void updateConfig(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", database::getJdbcUrl);
		registry.add("spring.datasource.username", database::getUsername);
		registry.add("spring.datasource.password", database::getPassword);
	}

	@Test
	void testOpenapiIsUpToDate() throws Exception {
		Assert.assertNotEquals(0, port);

		String url = "http://localhost:" + port + "/api/openapi?group=v1";
		String content = restTemplate.getForObject(url, String.class);

		String expected;
		try (InputStream input = this.getClass().getResourceAsStream("openapi_v1.json")) {
			expected = StreamUtils.copyToString(input, Charset.defaultCharset());
			expected = expected.replace("localhost:8080", "localhost:" + port);
		}
		
		Assert.assertEquals(expected, content);
	}
}
