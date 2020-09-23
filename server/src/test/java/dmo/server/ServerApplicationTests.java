package dmo.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dmo.server.api.v1.dto.*;
import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.event.AnimeListUpdated;
import dmo.server.integration.anidb.AnidbClient;
import dmo.server.repository.AnimeRepository;
import dmo.server.repository.AnimeStatusRepository;
import dmo.server.repository.UserRepository;
import dmo.server.security.DubUserDetails;
import dmo.server.security.GoogleAuthenticationService;
import dmo.server.service.AnimeUpdater;
import lombok.Data;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.authority.AuthorityUtils;
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

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AnimeStatusRepository animeStatusRepository;

    // Disable Anidb Integration
    @MockBean
    private AnidbClient anidbClient;

    @Autowired
    private AnimeUpdater animeUpdater;

    @SpyBean
    private GoogleAuthenticationService googleAuthenticationService;

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
    void openapiIsUpToDate() throws Exception {
        Assert.assertNotEquals(0, port);

        var url = "http://localhost:" + port + "/api/openapi?group=v1";
        var content = restTemplate.getForObject(url, String.class);

        final String expected;
        try (InputStream input = this.getClass().getResourceAsStream("openapi_v1.json")) {
            expected = StreamUtils.copyToString(input, Charset.defaultCharset())
                    .replaceAll("localhost:8080", "localhost:" + port)
                    .replaceAll("\\s*\\r?\\n?$", " ");
        }

        JSONAssert.assertEquals(expected, content, true);
    }

    @Test
    void userIsRegisteredAutomaticallyUponFirstLoginWithGoogle() {
        var jwtResponse = loginWithGoogleAuthenticationMock();

        Assert.assertNotNull(jwtResponse);
        Assert.assertNotNull(jwtResponse.getAccessToken());
        Assert.assertNotNull(jwtResponse.getExpiresIn());

        var user = userRepository.findByEmail(jwtResponse.getEmail());
        Assert.assertTrue(user.isPresent());
        Assert.assertNotNull(user.get().getId());
        Assert.assertEquals(jwtResponse.getEmail(), user.get().getEmail());
    }

    @Test
    void animeListIsStoredToDatabaseUponUpdate() {
        animeUpdater.onAnimeListUpdate(new AnimeListUpdated(
                Arrays.asList(
                        newAnime(1L, 1L, "星界の紋章", "Crest of the Stars"),
                        newAnime(2L, 1L, "サザンアイズ", "3x3 Eyes", "3x3 глаза"),
                        newAnime(3L, 1L, "3×3 EYES -聖魔伝説-",
                                "3x3 Eyes: Legend of the Divine Demon", "3х3 глаза: Сказание Сэймы")
                )
        ));

        long animeCount = animeRepository.count();
        Assert.assertEquals(3L, animeCount);

        var anime = animeRepository.findById(2L).get();
        Assert.assertEquals((Long) 2L, anime.getId());
        Assert.assertEquals(Anime.Type.UNKNOWN, anime.getType());
        Assert.assertEquals(3, anime.getTitles().size());
    }

    @Test
    void animeListRequiresAuthenticationViaApi() {
        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/anime?page=0&size=100",
                String.class
        );

        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void animeListIsRetrievedViaApi() {
        animeUpdater.onAnimeListUpdate(new AnimeListUpdated(
                Arrays.asList(
                        newAnime(1L, 1L, "星界の紋章", "Crest of the Stars"),
                        newAnime(2L, 1L, "サザンアイズ", "3x3 Eyes", "3x3 глаза"),
                        newAnime(3L, 1L, "3×3 EYES -聖魔伝説-",
                                "3x3 Eyes: Legend of the Divine Demon", "3х3 глаза: Сказание Сэймы")
                )
        ));

        var jwtResponse = loginWithGoogleAuthenticationMock();
        var headers = new HttpHeaders();
        headers.setBearerAuth(jwtResponse.getAccessToken());
        var httpEntity = new HttpEntity<>(headers);

        PageDto<AnimeLightDto> page = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/anime?page=0&size=100",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<PageDto<AnimeLightDto>>() {
                }
        ).getBody();

        Assert.assertEquals(0, page.getNumber());
        Assert.assertEquals(1, page.getTotalPages());
        Assert.assertEquals(100, page.getSize());
        Assert.assertEquals(3L, page.getTotalElements());
        Assert.assertEquals(3, page.getNumberOfElements());
        Assert.assertEquals(3, page.getContent().size());
        Assert.assertEquals(8, page.getContent().stream().mapToInt(a -> a.getTitles().size()).sum());
    }

    @Test
    void animeStatusUpdateRequiresAuthenticationViaApi() {
        var request = new UpdateAnimeStatusDto();
        request.setAnimeId(1L);
        request.setStatus(AnimeStatusDto.Status.IN_PROGRESS);

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/anime_status",
                request,
                String.class
        );

        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void animeStatusCanBeUpdatedViaApi() {
        animeUpdater.onAnimeListUpdate(new AnimeListUpdated(
                Arrays.asList(
                        newAnime(1L, 1L, "星界の紋章", "Crest of the Stars"),
                        newAnime(2L, 1L, "サザンアイズ", "3x3 Eyes", "3x3 глаза"),
                        newAnime(3L, 1L, "3×3 EYES -聖魔伝説-",
                                "3x3 Eyes: Legend of the Divine Demon", "3х3 глаза: Сказание Сэймы")
                )
        ));

        var jwtResponse = loginWithGoogleAuthenticationMock();
        var headers = new HttpHeaders();
        headers.setBearerAuth(jwtResponse.getAccessToken());

        var request = new UpdateAnimeStatusDto();
        request.setAnimeId(1L);
        request.setStatus(AnimeStatusDto.Status.IN_PROGRESS);

        var httpEntity = new HttpEntity<>(request, headers);

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/anime_status",
                httpEntity,
                AnimeStatusDto.class
        ).getBody();

        Assert.assertEquals((Long) 1L, response.getId());
        Assert.assertEquals(2, response.getTitles().size());
        Assert.assertEquals(AnimeDto.Type.UNKNOWN, response.getType());
        Assert.assertEquals(AnimeStatusDto.Status.IN_PROGRESS, response.getStatus());

        request.setStatus(AnimeStatusDto.Status.COMPLETED);
        response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/anime_status",
                httpEntity,
                AnimeStatusDto.class
        ).getBody();

        Assert.assertEquals((Long) 1L, response.getId());
        Assert.assertEquals(2, response.getTitles().size());
        Assert.assertEquals(AnimeDto.Type.UNKNOWN, response.getType());
        Assert.assertEquals(AnimeStatusDto.Status.COMPLETED, response.getStatus());
    }

    private JwtResponse loginWithGoogleAuthenticationMock() {
        Assert.assertNotEquals(0, port);

        var url = "http://localhost:" + port + "/login/google";
        var token = UUID.randomUUID().toString();
        var email = token + "@gmail.com";

        Mockito.doReturn(new DubUserDetails(
                null,
                email,
                AuthorityUtils.NO_AUTHORITIES,
                null
        )).when(googleAuthenticationService).fromIdToken(Mockito.eq(token));

        var body = new LinkedMultiValueMap<String, String>();
        body.add("id_token", token);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var request = new HttpEntity<>(body, headers);

        var result = restTemplate.postForObject(url, request, JwtResponse.class);
        result.setEmail(email);

        return result;
    }

    @Data
    public static class JwtResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private Long expiresIn;

        @JsonIgnore
        private String email;
    }

    private static Anime newAnime(Long id, Long episodeCount, String... titles) {
        var result = new Anime();

        result.setId(id);
        result.setEpisodeCount(episodeCount);
        result.setType(Anime.Type.UNKNOWN);

        var animeTitles = new HashSet<AnimeTitle>();
        for (int i = 0; i < titles.length; i++) {
            animeTitles.add(newAnimeTitle(i, titles[i]));
        }
        result.setTitles(animeTitles);

        return result;
    }

    private static final String[] LANGS = {"ja", "en", "ru", "fr", "es"};

    private static AnimeTitle newAnimeTitle(int index, String title) {
        var result = new AnimeTitle();

        result.setLang(LANGS[index]);
        result.setText(title);
        result.setType(AnimeTitle.Type.OFFICIAL);

        return result;
    }
}
