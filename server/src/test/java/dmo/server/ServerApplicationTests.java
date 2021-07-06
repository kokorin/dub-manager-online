package dmo.server;

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
import dmo.server.service.AnimeUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import retrofit2.mock.Calls;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static dmo.server.domain.Anime.Type.DELETED;
import static dmo.server.domain.Anime.Type.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
