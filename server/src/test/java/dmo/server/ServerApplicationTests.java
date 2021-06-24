package dmo.server;

import dmo.server.api.v1.dto.*;
import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.event.AnimeDeleted;
import dmo.server.event.AnimeListUpdated;
import dmo.server.integration.anidb.AnidbAnimeLightList;
import dmo.server.integration.anidb.AnidbAnimeUpdater;
import dmo.server.integration.anidb.AnidbClient;
import dmo.server.integration.anidb.MockAnidbConf;
import dmo.server.repository.AnimeRepository;
import dmo.server.repository.AnimeStatusRepository;
import dmo.server.repository.AnimeUpdateRepository;
import dmo.server.repository.UserRepository;
import dmo.server.security.DubUserDetails;
import dmo.server.security.GoogleAuthenticationService;
import dmo.server.security.JwtService;
import dmo.server.service.AnimeUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import retrofit2.mock.Calls;

import javax.transaction.Transactional;
import java.util.*;

import static dmo.server.domain.Anime.Type.DELETED;
import static dmo.server.domain.Anime.Type.UNKNOWN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = MockAnidbConf.class)
@SpringJUnitConfig
@Testcontainers
@TestPropertySource(properties = {
        "logging.level.ROOT=INFO",
        "dmo.anidb.client=test",
        "dmo.anidb.client.version=1",
        "google.oauth.client.id=123",
        "spring.main.allow-bean-definition-overriding=true"
})
class ServerApplicationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimeStatusRepository animeStatusRepository;

    @Autowired
    private AnimeUpdateRepository animeUpdateRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AnidbAnimeUpdater anidbAnimeUpdater;

    // Disable Anidb Integration
    @MockBean
    private AnidbClient anidbClient;

    @Autowired
    private AnimeUpdater animeUpdater;

    @SpyBean
    private GoogleAuthenticationService googleAuthenticationService;

    @Autowired
    private JwtService jwtService;

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
    public void clearAnimeTable() {
        animeStatusRepository.deleteAll();
        animeUpdateRepository.deleteAll();
        animeRepository.deleteAll();
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
        assertEquals(3L, animeCount);

        var anime = animeRepository.findById(2L).orElse(null);
        assertNotNull(anime);
        assertEquals((Long) 2L, anime.getId());
        assertEquals(Anime.Type.UNKNOWN, anime.getType());
        assertEquals(3, anime.getTitles().size());
    }

    @Test
    @Transactional
    public void animeIsMarkedDeletedIfAnimeDeletedEvent() {
        var anime = new Anime();
        anime.setId(777L);
        anime.setType(Anime.Type.UNKNOWN);

        animeRepository.saveAndFlush(anime);

        animeUpdater.onAnimeDeleted(new AnimeDeleted(777L));

        animeRepository.flush();
        anime = animeRepository.getById(777L);

        assertNotNull(anime);
        assertEquals(DELETED, anime.getType());
    }

    @Test
    void animeListRequiresAuthenticationViaApi() {
        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/anime?page=0&size=100",
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
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

        PageDto<AnimeDto> page = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/anime?page=0&size=100",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<PageDto<AnimeDto>>() {
                }
        ).getBody();

        assertNotNull(page);
        assertEquals(0, page.getNumber());
        assertEquals(1, page.getTotalPages());
        assertEquals(100, page.getSize());
        assertEquals(3L, page.getTotalElements());
        assertEquals(3, page.getNumberOfElements());
        assertEquals(3, page.getContent().size());
        assertEquals(8, page.getContent().stream().mapToInt(a -> a.getTitles().size()).sum());
    }

    @Test
    void animeStatusUpdateRequiresAuthenticationViaApi() {
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);
        var animeId = 1L;

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/anime_status/" + animeId,
                request,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void clientConfigurationDoesntRequireAuthentication() {
        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/conf",
                ConfigurationDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var config = response.getBody();
        assertNotNull(config);
        assertEquals("123", config.getGoogleOAuthClientId());
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

        var animeId = 1L;
        var request = new UpdateAnimeStatusDto();
        request.setProgress(AnimeProgressDto.IN_PROGRESS);

        var httpEntity = new HttpEntity<>(request, headers);

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId,
                httpEntity,
                AnimeStatusDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), response.toString());

        var animeStatus = response.getBody();

        assertNotNull(animeStatus);
        assertEquals((Long) animeId, animeStatus.getAnime().getId());
        assertEquals(2, animeStatus.getAnime().getTitles().size());
        assertEquals(AnimeTypeDto.UNKNOWN, animeStatus.getAnime().getType());
        assertEquals(AnimeProgressDto.IN_PROGRESS, animeStatus.getProgress());

        request.setProgress(AnimeProgressDto.COMPLETED);
        animeStatus = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/users/current/anime/" + animeId,
                httpEntity,
                AnimeStatusDto.class
        ).getBody();

        assertEquals((Long) animeId, animeStatus.getAnime().getId());
        assertEquals(2, animeStatus.getAnime().getTitles().size());
        assertEquals(AnimeTypeDto.UNKNOWN, animeStatus.getAnime().getType());
        assertEquals(AnimeProgressDto.COMPLETED, animeStatus.getProgress());
    }

    @Test
    public void scheduledAnimeListUpdate() {
        var call = Calls.response(new AnidbAnimeLightList());
        Mockito.when(anidbClient.getAnimeList()).thenReturn(call);

        animeUpdater.scheduleAnimeListUpdate();

        Mockito.verify(anidbClient).getAnimeList();
        Mockito.verifyNoMoreInteractions(anidbClient);
    }

    @Test
    public void scheduledAnimeUpdate() {
        animeRepository.saveAllAndFlush(List.of(
                newAnime(4L, Anime.Type.DELETED, null, "Marked deleted, should be skipped"),
                newAnime(3L, null, "To be updated first"),
                newAnime(2L, null, "Should not be updated"),
                newAnime(1L, null, "Should not be updated too")
        ));

        animeUpdater.scheduleAnimeUpdate();
        anidbAnimeUpdater.scheduledUpdateAnime();

        Mockito.verify(anidbClient).getAnime(Mockito.eq(3L), Mockito.anyString(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(anidbClient);
    }

    private JwtResponse loginWithGoogleAuthenticationMock() {
        assertNotEquals(0, port);

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
        result.setUserDetails(jwtService.fromJwt(result.getAccessToken()));

        return result;
    }

    private static Anime newAnime(Long id, Long episodeCount, String... titles) {
        return newAnime(id, UNKNOWN, episodeCount, titles);
    }

    private static Anime newAnime(Long id, Anime.Type type, Long episodeCount, String... titles) {
        var result = new Anime();

        result.setId(id);
        result.setEpisodeCount(episodeCount);
        result.setType(type);

        var animeTitles = new HashSet<AnimeTitle>();
        for (int i = 0; i < titles.length; i++) {
            animeTitles.add(newAnimeTitle(i, titles[i]));
        }
        result.setTitles(animeTitles);

        return result;
    }

    private static final String[] LANGUAGES = {"ja", "en", "ru", "fr", "es"};

    private static AnimeTitle newAnimeTitle(int index, String title) {
        var result = new AnimeTitle();

        result.setLang(LANGUAGES[index]);
        result.setText(title);
        result.setType(AnimeTitle.Type.OFFICIAL);

        return result;
    }
}
