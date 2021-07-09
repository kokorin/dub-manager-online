package dmo.server;

import dmo.server.api.v1.dto.*;
import dmo.server.integration.anidb.MockAnidbConf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        "google.oauth.client.id=GoogleAuthClientId",
        "spring.main.allow-bean-definition-overriding=true",
        "anime.update.delay=PT1S"
})
public class ApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static boolean animeListUpdated = false;

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

    @BeforeEach
    public void waitUntilAnimeListIsUpdated() throws InterruptedException {
        if (!animeListUpdated) {
            animeListUpdated = true;
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    void clientConfigurationDoesntRequireAuthentication() {
        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/conf",
                ConfigurationDto.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        var config = response.getBody();
        assertNotNull(config);
        assertEquals("GoogleAuthClientId", config.getGoogleOAuthClientId());
    }

    @Test
    void currentUserRequiresLogin() {
        var url = "http://localhost:" + port + "/api/v1/users/current";
        var response = restTemplate.getForEntity(url, Void.class);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void userIsRegisteredAutomaticallyUponFirstLoginWithGoogle() {
        var authHeaders = loginWithGoogleAuthenticationMock();

        var url = "http://localhost:" + port + "/api/v1/users/current";
        var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(authHeaders), UserDto.class);
        assertNotNull(response);
        var user = response.getBody();
        assertNotNull(user);
        assertNotNull(user.getEmail());

        assertThat(user.getEmail(), endsWith("@dub-manager.online"));
    }

    @Test
    void animeListRequiresAuthentication() {
        var url = "http://localhost:" + port + "/api/v1/anime?page=0&size=100";
        var response = restTemplate.getForEntity(url, Void.class);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void animeListIsUpdated() throws InterruptedException {
        var authHeaders = loginWithGoogleAuthenticationMock();

        var url = "http://localhost:" + port + "/api/v1/anime?page=0&size=100";
        var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(authHeaders),
                new ParameterizedTypeReference<PageDto<AnimeDto>>() {
                });
        assertNotNull(response);

        var page = response.getBody();
        assertNotNull(page);
        assertEquals(6L, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertEquals(100, page.getSize());

        var content = page.getContent();
        assertNotNull(content);
        assertEquals(6, content.size());

        var titlePresent = content.stream()
                .flatMap(a -> a.getTitles().stream())
                .anyMatch(t -> "Crest of the Stars".equals(t.getText()));
        assertTrue(titlePresent);
    }

    @Test
    void animeStatusRequiresUpdate() throws InterruptedException {
        var animeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        request.setComment("Comment can be stored!");

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId,
                new HttpEntity<>(request, HttpHeaders.EMPTY),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void animeStatusCanBeUpdated() throws InterruptedException {
        var authHeaders = loginWithGoogleAuthenticationMock();
        var animeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        request.setComment("Comment can be stored!");

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId,
                new HttpEntity<>(request, authHeaders),
                AnimeStatusDto.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), response.toString());

        var animeStatus = response.getBody();

        assertNotNull(animeStatus);
        assertEquals((Long) animeId, animeStatus.getAnime().getId());
        assertEquals(10, animeStatus.getAnime().getTitles().size());
        assertEquals(AnimeTypeDto.UNKNOWN, animeStatus.getAnime().getType());
        assertEquals(AnimeProgressDto.IN_PROGRESS, animeStatus.getProgress());
        assertEquals("Comment can be stored!", animeStatus.getComment());

        request.setProgress(AnimeProgressDto.COMPLETED);
        response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId,
                new HttpEntity<>(request, authHeaders),
                AnimeStatusDto.class
        );
        assertNotNull(response);

        animeStatus = response.getBody();
        assertNotNull(animeStatus);
        assertEquals((Long) animeId, animeStatus.getAnime().getId());
        assertEquals(10, animeStatus.getAnime().getTitles().size());
        assertEquals(AnimeTypeDto.UNKNOWN, animeStatus.getAnime().getType());
        assertEquals(AnimeProgressDto.COMPLETED, animeStatus.getProgress());
        assertEquals("Comment can be stored!", animeStatus.getComment());
    }

    @Test
    void episodeStatusRequiresAuthentication() throws InterruptedException {
        var animeId = 1L;

        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId + "/episode/42",
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void episodeStatusNotAvailableIfNoAnimeStatusSet() throws InterruptedException {
        var authHeaders = loginWithGoogleAuthenticationMock();
        var animeId = 1L;

        var response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId + "/episode/42",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void episodeStatusIsSetToNotStartedByDefault() throws InterruptedException {
        var authHeaders = loginWithGoogleAuthenticationMock();

        var animeId = 1L;
        var episodeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        request.setComment("Electrophoresis!");

        var animeResponse = restTemplate.postForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                new HttpEntity<>(request, authHeaders),
                AnimeStatusDto.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(animeResponse);
        assertEquals(HttpStatus.OK, animeResponse.getStatusCode(), animeResponse.toString());

        // Wait until anime is fetched and episodes are stored
        TimeUnit.SECONDS.sleep(2);

        var episodesResponse = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}/episodes?page=0&size=100",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                new ParameterizedTypeReference<PageDto<EpisodeStatusDto>>() {
                },
                Map.of("port", port, "animeId", animeId, "episodeId", episodeId)
        );

        assertNotNull(episodesResponse);
        assertEquals(HttpStatus.OK, episodesResponse.getStatusCode());

        var page = episodesResponse.getBody();
        assertNotNull(page);
        assertEquals(100, page.getSize());
        assertEquals(17, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals(0, page.getNumber());

        var episodes = page.getContent();
        assertEquals(17, episodes.size());
        var distinctProgress = episodes.stream().map(EpisodeStatusDto::getProgress).collect(Collectors.toSet());
        assertEquals(Collections.singleton(EpisodeProgressDto.NOT_STARTED), distinctProgress);
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
