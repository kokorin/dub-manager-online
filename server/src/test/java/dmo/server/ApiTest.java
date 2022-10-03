package dmo.server;

import com.nimbusds.jose.JOSEObjectType;
import com.p6spy.engine.spy.P6SpyDriver;
import dmo.server.api.v1.dto.*;
import dmo.server.domain.Anime;
import dmo.server.event.AnimeListUpdateScheduled;
import dmo.server.event.AnimeUpdateScheduled;
import dmo.server.integration.anidb.MockAnidbConf;
import dmo.server.okhttp.InMemoryCookieJar;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.Driver;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MockAnidbConf.class)
@SpringJUnitConfig
@Testcontainers
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-test.properties"})
@Slf4j
public class ApiTest {

    @LocalServerPort
    private int port;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private static final DockerImageName MARIA_ALPINE = DockerImageName
            .parse("yobasystems/alpine-mariadb")
            .asCompatibleSubstituteFor("mariadb");

    @Container
    public static JdbcDatabaseContainer<?> database = new MariaDBContainer<>(MARIA_ALPINE)
            .withReuse(true)
            .withEnv("MYSQL_CHARSET", "utf8mb4")
            .withEnv("MYSQL_COLLATION", "utf8mb4_general_ci");


    private static final String ISSUER = "mock-auth";

    private static final MockOAuth2Server mockOAuth2Server = new MockOAuth2Server();

    @BeforeAll
    public static void startMockOAuth() throws IOException {
        mockOAuth2Server.start();
    }

    @DynamicPropertySource
    public static void updateConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", P6SpyDriver.class::getName);
        registry.add("spring.datasource.url",
                () -> database.getJdbcUrl().replaceFirst("jdbc:", "jdbc:p6spy:"));
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);

        registry.add("spring.security.oauth2.client.provider." + ISSUER + ".issuer-uri",
                () -> mockOAuth2Server.issuerUrl(ISSUER).toString());
        registry.add("spring.security.oauth2.client.registration." + ISSUER + ".client-id",
                () -> "random-client-id");
        registry.add("spring.security.oauth2.client.registration." + ISSUER + ".client-secret",
                () -> "random-client-secret");
        registry.add("spring.security.oauth2.client.registration." + ISSUER + ".scope",
                () -> "profile,email,openid");
    }

    private static boolean animeListUpdated = false;

    @BeforeEach
    public void waitUntilAnimeListIsUpdated() throws InterruptedException {
        if (!animeListUpdated) {
            animeListUpdated = true;

            TimeUnit.SECONDS.sleep(1);

            log.info("Initializing anime list");
            Stream.of(1L, 2L, 3L, 357L, 979L, 11681L)
                    .map(id -> {
                        var a = new Anime();
                        a.setId(id);
                        return a;
                    })
                    .map(AnimeUpdateScheduled::new)
                    .forEach(eventPublisher::publishEvent);
            TimeUnit.SECONDS.sleep(3);
            log.info("Initialization complete");
        }
    }

    @Test
    void confOAuthClients() {
        var restTemplate = getRestTemplate(false);
        var url = "http://localhost:" + port + "/api/v1/conf/oauth/clients";
        var response = restTemplate.getForEntity(url, Set.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var clients = response.getBody();
        assertNotNull(clients);
        assertEquals(Set.of("google", ISSUER), clients);
    }

    @Test
    void currentUserRequiresLogin() {
        var restTemplate = getRestTemplate(false);
        var url = "http://localhost:" + port + "/api/v1/users/current";
        var response = restTemplate.getForEntity(url, Void.class);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void userIsRegisteredAutomaticallyUponFirstLogin() {
        var restTemplate = getRestTemplate(true);

        var url = "http://localhost:" + port + "/api/v1/users/current";
        var response = restTemplate.getForEntity(url, UserDto.class);
        assertNotNull(response);
        var user = response.getBody();
        assertNotNull(user);
        assertNotNull(user.getEmail());

        assertThat(user.getEmail(), endsWith("@dub-manager.online"));
    }

    @Test
    void animeListRequiresAuthentication() {
        var restTemplate = getRestTemplate(false);
        var url = "http://localhost:" + port + "/api/v1/anime?page=0&size=100";
        var response = restTemplate.getForEntity(url, Void.class);
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void animeListIsUpdated() throws InterruptedException {
        var restTemplate = getRestTemplate(true);

        var url = "http://localhost:" + port + "/api/v1/anime?page=0&size=100";
        var response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY,
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
    void animeStatusUpdateRequiresAuthentication() throws InterruptedException {
        var restTemplate = getRestTemplate(false);
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
        var restTemplate = getRestTemplate(true);
        var animeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        request.setComment("Comment can be stored!");

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId,
                new HttpEntity<>(request),
                AnimeStatusDto.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), response.toString());

        var animeStatus = response.getBody();

        assertNotNull(animeStatus);
        assertEquals((Long) animeId, animeStatus.getAnime().getId());
        assertEquals(10, animeStatus.getAnime().getTitles().size());
        assertEquals(AnimeTypeDto.TV_SERIES, animeStatus.getAnime().getType());
        assertEquals(AnimeProgressDto.IN_PROGRESS, animeStatus.getProgress());
        assertEquals("Comment can be stored!", animeStatus.getComment());

        request.setProgress(AnimeProgressDto.COMPLETED);
        response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId,
                new HttpEntity<>(request),
                AnimeStatusDto.class
        );
        assertNotNull(response);

        animeStatus = response.getBody();
        assertNotNull(animeStatus);
        assertEquals((Long) animeId, animeStatus.getAnime().getId());
        assertEquals(10, animeStatus.getAnime().getTitles().size());
        assertEquals(AnimeTypeDto.TV_SERIES, animeStatus.getAnime().getType());
        assertEquals(AnimeProgressDto.COMPLETED, animeStatus.getProgress());
        assertEquals("Comment can be stored!", animeStatus.getComment());
    }

    @Test
    void deleteAnimeStatusRequiresAuthentication() {
        var restTemplate = getRestTemplate(false);
        var response = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                Map.of("port", port, "animeId", 42)
        );

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void deleteAnimeStatusReturnsNotFoundIfAnimeStatusIsAbsent() {
        var restTemplate = getRestTemplate(true);
        var response = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                Map.of("port", port, "animeId", 42)
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void animeStatusCanBeDeleted() {
        var restTemplate = getRestTemplate(true);

        var animeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        request.setComment("Electrophoresis!");

        var createResponse = restTemplate.postForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                new HttpEntity<>(request),
                AnimeStatusDto.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(createResponse);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), createResponse.toString());

        var deleteResponse = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        var checkResponse = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(checkResponse);
        assertEquals(HttpStatus.NOT_FOUND, checkResponse.getStatusCode());
    }

    @Test
    void animeStatusRequiresAuthentication() {
        var restTemplate = getRestTemplate(false);

        var animeId = 1L;

        var getResponse = restTemplate.getForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                AnimeStatusDto.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(getResponse);
        assertEquals(HttpStatus.UNAUTHORIZED, getResponse.getStatusCode());
    }

    @Test
    void animeStatusIsRetrievedById() {
        var restTemplate = getRestTemplate(true);

        var animeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        request.setComment("animeStatusIsRetrievedById!");

        var createResponse = restTemplate.postForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                new HttpEntity<>(request),
                AnimeStatusDto.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(createResponse);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), createResponse.toString());

        var getResponse = restTemplate.getForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                AnimeStatusDto.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(getResponse);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode(), getResponse.toString());

        var animeStatus = getResponse.getBody();
        assertNotNull(animeStatus);
        assertNotNull(animeStatus.getAnime());
        assertEquals(animeId, animeStatus.getAnime().getId());
        assertEquals(AnimeProgressDto.IN_PROGRESS, animeStatus.getProgress());
    }

    @Test
    void episodeStatusesRequireAuthentication() throws InterruptedException {
        var restTemplate = getRestTemplate(false);
        var animeId = 1L;

        var response = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}/episodes?page=0&size=100",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<PageDto<AnimeStatusDto>>() {
                },
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void episodeStatusesNotAvailableIfNoAnimeStatusSet() throws InterruptedException {
        var restTemplate = getRestTemplate(true);

        var response = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/1/episodes?page=0&size=100",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<PageDto<AnimeStatusDto>>() {
                },
                Map.of("port", port)
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void episodeStatusesAreSetToNotStartedByDefault() {
        var restTemplate = getRestTemplate(true);

        var animeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        request.setComment("Electrophoresis!");

        var animeResponse = restTemplate.postForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                new HttpEntity<>(request),
                AnimeStatusDto.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(animeResponse);
        assertEquals(HttpStatus.OK, animeResponse.getStatusCode(), animeResponse.toString());

        var episodesResponse = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}/episodes?page=0&size=100",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<PageDto<EpisodeStatusDto>>() {
                },
                Map.of("port", port, "animeId", animeId)
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
    void episodeStatusesCanBeFilteredByType() throws InterruptedException {
        var restTemplate = getRestTemplate(true);

        var animeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        request.setComment("Will test filtering!");

        var animeResponse = restTemplate.postForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                new HttpEntity<>(request),
                AnimeStatusDto.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(animeResponse);
        assertEquals(HttpStatus.OK, animeResponse.getStatusCode(), animeResponse.toString());

        var episodesResponse = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}/episodes?page=0&size=100&type={type}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<PageDto<EpisodeStatusDto>>() {
                },
                Map.of("port", port, "animeId", animeId, "type", EpisodeTypeDto.REGULAR)
        );

        assertNotNull(episodesResponse);
        assertEquals(HttpStatus.OK, episodesResponse.getStatusCode());

        var page = episodesResponse.getBody();
        assertNotNull(page);
        assertEquals(100, page.getSize());
        assertEquals(13, page.getTotalElements());
        assertEquals(13, page.getContent().size());
    }

    @Test
    void episodeStatusUpdateRequiresAuthentication() throws InterruptedException {
        var restTemplate = getRestTemplate(false);
        var animeId = 1L;
        var updateEpisodeStatus = new UpdateEpisodeStatusDto();
        updateEpisodeStatus.setProgress(EpisodeProgressDto.COMPLETED);

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/{animeId}/episodes/42",
                updateEpisodeStatus,
                EpisodeStatusDto.class,
                Map.of("animeId", animeId)
        );

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void episodeStatusCanBeUpdated() throws InterruptedException {
        var restTemplate = getRestTemplate(true);
        var animeId = 979L;
        var episodeId = 11593L;

        var updateAnimeStatus = new UpdateAnimeStatusDto();
        updateAnimeStatus.setProgress(AnimeProgressDto.IN_PROGRESS);
        updateAnimeStatus.setComment("Abra-Kadabra!");

        var animeResponse = restTemplate.postForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}",
                new HttpEntity<>(updateAnimeStatus),
                AnimeStatusDto.class,
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(animeResponse);
        assertEquals(HttpStatus.OK, animeResponse.getStatusCode(), animeResponse.toString());
        var animeStatus = animeResponse.getBody();
        assertNotNull(animeStatus);
        assertEquals(animeId, animeStatus.getAnime().getId());
        assertEquals(51L, animeStatus.getRegularEpisodeTotalCount());
        assertEquals(0L, animeStatus.getRegularEpisodeCompleteCount());
        assertEquals(LocalDate.of(2003, 10, 4), animeStatus.getRegularEpisodeNextAirDate());

        var updateEpisodeStatus = new UpdateEpisodeStatusDto();
        updateEpisodeStatus.setProgress(EpisodeProgressDto.COMPLETED);

        var response = restTemplate.postForEntity(
                "http://localhost:{port}/api/v1/users/current/anime/{animeId}/episodes/{episodeId}",
                updateEpisodeStatus,
                EpisodeStatusDto.class,
                Map.of("port", port, "animeId", animeId, "episodeId", episodeId)
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), response.toString());

        var updated = response.getBody();
        assertNotNull(updated);
        assertNotNull(updated.getEpisode());
        assertEquals(episodeId, updated.getEpisode().getId());
        assertNotNull(updated.getEpisode().getTitles());
        assertEquals(EpisodeProgressDto.COMPLETED, updated.getProgress());

        var animePageResponse = restTemplate.exchange(
                "http://localhost:{port}/api/v1/users/current/anime/?page=0&size=100",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<PageDto<AnimeStatusDto>>() {
                },
                Map.of("port", port, "animeId", animeId)
        );

        assertNotNull(animePageResponse);
        assertEquals(HttpStatus.OK, animePageResponse.getStatusCode(), animePageResponse.toString());

        var animePage = animePageResponse.getBody();
        assertNotNull(animePage);
        assertEquals(1L, animePage.getTotalElements());

        var animeStatuses = animePage.getContent();
        assertNotNull(animeStatuses);
        assertEquals(1, animeStatuses.size());
        animeStatus = animeStatuses.get(0);

        assertEquals(51L, animeStatus.getRegularEpisodeTotalCount());
        assertEquals(1L, animeStatus.getRegularEpisodeCompleteCount());
        assertEquals(LocalDate.of(2003, 10, 11), animeStatus.getRegularEpisodeNextAirDate());
    }

    @Test
    void openapiIsUpToDate() throws Exception {
        var restTemplate = getRestTemplate(false);
        assertNotEquals(0, port);

        var url = "http://localhost:" + port + "/api/openapi/v1";
        var content = restTemplate.getForObject(url, String.class);

        final String expected;
        try (InputStream input = this.getClass().getResourceAsStream("openapi_v1.json")) {
            expected = StreamUtils.copyToString(input, Charset.defaultCharset())
                    .replaceAll("localhost:8080", "localhost:" + port);
        }

        JSONAssert.assertEquals("Actual:\n" + content, expected, content, true);
    }

    @SneakyThrows
    private TestRestTemplate getRestTemplate(boolean authorize) {
        var restTemplate = new TestRestTemplate(new RestTemplateBuilder()
                .requestFactory(() -> new OkHttp3ClientHttpRequestFactory(
                        new OkHttpClient(new OkHttpClient.Builder()
                                .followRedirects(true)
                                .cookieJar(new InMemoryCookieJar())
                        )
                )));

        if (authorize) {
            var username = UUID.randomUUID().toString();
            var email = username + "@dub-manager.online";

            mockOAuth2Server.enqueueCallback(new DefaultOAuth2TokenCallback(
                    ISSUER,
                    username,
                    JOSEObjectType.JWT.getType(),
                    List.of("audience"),
                    Map.of("email", email),
                    3600
            ));

            var authorizationResponse = restTemplate.getForEntity(
                    "http://localhost:{port}/oauth2/authorization/{issuer}",
                    String.class,
                    Map.of("port", port, "issuer", ISSUER)
            );
        }

        return restTemplate;
    }
}
