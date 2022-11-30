package dmo.server.integration.anidb.client;

import dmo.server.integration.anidb.dto.*;
import dmo.server.junit.MockWebServerExtension;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@SpringBootTest(classes = {AnidbClientConfiguration.class})
@ExtendWith(MockWebServerExtension.class)
class AnidbClientTest {

    private static MockWebServer mockWebServer;

    @Autowired
    private AnidbClient anidbClient;
    @Autowired
    private AnidbTitlesClient anidbTitlesClient;

    @BeforeAll
    public static void setMockWebServer(MockWebServer mockWebServer) {
        AnidbClientTest.mockWebServer = mockWebServer;
    }

    @DynamicPropertySource
    public static void initMockWebServer(DynamicPropertyRegistry registry) {
        registry.add("integration.anidb.titles.base_url", () -> mockWebServer.url("").toString());
        registry.add("integration.anidb.api.base_url", () -> mockWebServer.url("").toString());
    }

    @Test
    void getAnimeList() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody(new Buffer().readFrom(getClass().getResourceAsStream("anime-titles.xml")))
        );
        // mockResponseInterceptor.useResponseFileOnce("anime-titles.xml.gz");
        List<AnidbAnimeTitles> animeList = anidbTitlesClient.getAnimeList().execute().body().animeList;

        AnidbAnimeTitles anime = animeList.get(0);
        assertEquals(Long.valueOf(1L), anime.id);
        assertEquals(10, anime.titles.size());

        var title = new AnidbAnimeTitle();
        title.type = AnidbAnimeTitle.Type.SHORT;
        title.lang = "en";
        title.text = "CotS";
        assertThat(anime.titles, Matchers.hasItem(title));

        List<AnidbAnimeTitles> nonParsedAnimeList = animeList.stream()
                .filter(a -> isEmpty(a.id) || isEmpty(a.titles))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedAnimeList, "Anime should be parsed");

        List<AnidbAnimeTitle> nonParsedAnimeTitleList = animeList.stream()
                .map(a -> a.titles)
                .flatMap(Set::stream)
                .filter(t -> isEmpty(t.type) || isEmpty(t.lang) || isEmpty(t.text))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedAnimeTitleList, "Anime titles should be parsed");

    }

    @Test
    void getAnime() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody(new Buffer().readFrom(getClass().getResourceAsStream("anime-979.xml")))
        );
        // mockResponseInterceptor.useResponseFileOnce("anime-979.xml");
        var anime = anidbClient.getAnime(979L, "mock", "mock").execute().body();

        assertNotNull(anime);
        assertEquals((Long) 979L, anime.id);
        assertEquals(LocalDate.of(2003, 10, 4), anime.startDate);
        assertEquals(LocalDate.of(2004, 10, 2), anime.endDate);
        assertEquals((Long) 51L, anime.episodeCount);
        assertEquals(56, anime.titles.size());

        validateTitlesAndEpisodes(anime);
    }

    @Test
    void getAnimeStrangeEpisodeNumbers() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody(new Buffer().readFrom(getClass().getResourceAsStream("anime-11681.xml")))
        );
        //mockResponseInterceptor.useResponseFileOnce("anime-11681.xml");
        var anime = anidbClient.getAnime(11681L, "mock", "mock").execute().body();

        assertNotNull(anime);
        assertEquals((Long) 11681L, anime.id);
        assertEquals(LocalDate.of(2017, 2, 18), anime.startDate);
        assertEquals(LocalDate.of(2017, 2, 18), anime.endDate);
        assertEquals((Long) 1L, anime.episodeCount);
        assertEquals(10, anime.titles.size());

        validateTitlesAndEpisodes(anime);
    }

    private static void validateTitlesAndEpisodes(AnidbAnime anime) {
        List<AnidbAnimeTitle> nonParsedTitles = anime.titles.stream()
                .filter(t -> isEmpty(t.type) || isEmpty(t.lang) || isEmpty(t.text))
                .collect(Collectors.toList());
        assertThat("Anime titles should be parsed", nonParsedTitles, empty());

        List<AnidbEpisode> nonParsedEpisodes = anime.episodes.stream()
                .filter(e -> isEmpty(e.id) || isEmpty(e.length) || isEmpty(e.epno)
                        || isEmpty(e.epno.getNumber()) || isEmpty(e.epno.getType()))
                .collect(Collectors.toList());
        assertThat("Anime episodes should be parsed", nonParsedEpisodes, empty());

        List<AnidbEpisodeTitle> nonParsedEpisodeTitles = anime.episodes.stream()
                .map(e -> e.titles)
                .flatMap(List::stream)
                .filter(t -> isEmpty(t.lang) || isEmpty(t.text))
                .collect(Collectors.toList());

        assertThat("Anime episode titles should be parsed", nonParsedEpisodeTitles, empty());
    }
}