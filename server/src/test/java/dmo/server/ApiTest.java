package dmo.server;

import dmo.server.api.v1.dto.UserDto;
import dmo.server.integration.anidb.MockAnidbConf;
import org.hamcrest.BaseMatcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.CookieAssertions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MockAnidbConf.class)
@SpringJUnitConfig
@Testcontainers
@TestPropertySource(properties = {
        "logging.level.ROOT=INFO",
        "dmo.anidb.client=test",
        "dmo.anidb.client.version=1",
        "google.oauth.client.id=123",
        "spring.main.allow-bean-definition-overriding=true"
})
public class ApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final DockerImageName MARIA_ALPINE = DockerImageName
            .parse("yobasystems/alpine-mariadb")
            .asCompatibleSubstituteFor("mariadb");

    @Container
    public static JdbcDatabaseContainer<?> database = new MariaDBContainer<>(MARIA_ALPINE)
            .withReuse(true)
            .withEnv("MYSQL_CHARSET", "utf8mb4")
            .withEnv("MYSQL_COLLATION", "utf8mb4_general_ci");

    @DynamicPropertySource
    public static void updateConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    @Test
    void userIsRegisteredAutomaticallyUponFirstLoginWithGoogle() {
        var authHeaders = loginWithGoogleAuthenticationMock();

        var url = "http://localhost:" + port + "/api/v1/users/current";
        var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(authHeaders), UserDto.class);
        assertNotNull(response);
        var user = response.getBody();
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getEmail());

        assertThat(user.getEmail(), endsWith("@dub-manager.online"));
    }

    @Test
    void openapiIsUpToDate() throws Exception {
        assertNotEquals(0, port);

        var url = "http://localhost:" + port + "/api/openapi?group=v1";
        var content = restTemplate.getForObject(url, String.class);

        final String expected;
        try (InputStream input = this.getClass().getResourceAsStream("openapi_v1.json")) {
            expected = StreamUtils.copyToString(input, Charset.defaultCharset())
                    .replaceAll("localhost:8080", "localhost:" + port)
                    .replaceAll("\\s*\\r?\\n?$", " ");
        }

        JSONAssert.assertEquals("Actual:\n" + content, expected, content, true);
    }

    private HttpHeaders loginWithGoogleAuthenticationMock() {
        var url = "http://localhost:" + port + "/login/google";
        var email = UUID.randomUUID() + "@dub-manager.online";

        var body = new LinkedMultiValueMap<String, String>();
        body.add("id_token", email);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var request = new HttpEntity<>(body, headers);

        var jwtResponse = restTemplate.postForObject(url, request, JwtResponse.class);

        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getAccessToken());
        assertNotNull(jwtResponse.getExpiresIn());

        var resultHeaders = new HttpHeaders();
        resultHeaders.setBearerAuth(jwtResponse.getAccessToken());
        return resultHeaders;
    }
}
