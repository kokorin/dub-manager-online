package dmo.server.integration.anidb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@SpringBootTest(
        classes = {AnidbConfig.class, MockAnidbConf.class},
        properties = "spring.main.allow-bean-definition-overriding=true")
class AnidbClientTest {

    @Autowired
    private AnidbClient anidbClient;

    @Autowired
    private MockResponseInterceptor mockResponseInterceptor;

    @Test
    void getAnimeList() throws Exception {
        mockResponseInterceptor.useResponseFileOnce("anime-titles.xml.gz");
        List<AnidbAnimeLight> animeList = anidbClient.getAnimeList().execute().body().animeList;

        AnidbAnimeLight anime = animeList.get(0);
        assertEquals(Long.valueOf(1L), anime.id);
        assertEquals(10, anime.titles.size());

        AnidbAnimeTitle title = anime.titles.get(0);
        assertEquals(AnidbAnimeTitle.Type.SHORT, title.type);
        assertEquals("en", title.lang);
        assertEquals("CotS", title.text);

        List<AnidbAnimeLight> nonParsedAnimeList = animeList.stream()
                .filter(a -> isEmpty(a.id) || isEmpty(a.titles))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedAnimeList, "Anime should be parsed");

        List<AnidbAnimeTitle> nonParsedAnimeTitleList = animeList.stream()
                .map(a -> a.titles)
                .flatMap(List::stream)
                .filter(t -> isEmpty(t.type) || isEmpty(t.lang) || isEmpty(t.text))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedAnimeTitleList, "Anime titles should be parsed");

    }

    @Test
    void getAnime() throws Exception {
        mockResponseInterceptor.useResponseFileOnce("anime-979.xml");
        var apiResponse = anidbClient.getAnime(979L, "mock", "mock").execute().body();
        assertTrue(apiResponse instanceof AnidbAnime);

        var anime = (AnidbAnime) apiResponse;
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
        mockResponseInterceptor.useResponseFileOnce("anime-11681.xml");
        var apiResponse = anidbClient.getAnime(11681L, "mock", "mock").execute().body();
        assertTrue(apiResponse instanceof AnidbAnime);

        var anime = (AnidbAnime) apiResponse;

        assertNotNull(anime);
        assertEquals((Long) 11681L, anime.id);
        assertEquals(LocalDate.of(2017, 2, 18), anime.startDate);
        assertEquals(LocalDate.of(2017, 2, 18), anime.endDate);
        assertEquals((Long) 1L, anime.episodeCount);
        assertEquals(10, anime.titles.size());

        validateTitlesAndEpisodes(anime);
    }

    @Test
    void getErrorCodeAndMessage() throws Exception {
        mockResponseInterceptor.useResponseFileOnce("api-error.xml");
        var apiResponse = anidbClient.getAnime(42L, "mock", "mock").execute().body();

        assertTrue(apiResponse instanceof AnidbError);
        var error = (AnidbError) apiResponse;

        assertEquals((Integer) 302, error.code);
        assertEquals("client version missing or invalid", error.message);
    }

    private static void validateTitlesAndEpisodes(AnidbAnime anime) {
        List<AnidbAnimeTitle> nonParsedTitles = anime.titles.stream()
                .filter(t -> isEmpty(t.type) || isEmpty(t.lang) || isEmpty(t.text))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedTitles, "Anime titles should be parsed");

        List<AnidbEpisode> nonParsedEpisodes = anime.episodes.stream()
                .filter(e -> isEmpty(e.id) || isEmpty(e.length) || isEmpty(e.epno)
                        || isEmpty(e.epno.getNumber()) || isEmpty(e.epno.getType()))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedEpisodes, "Anime episodes should be parsed");

        List<AnidbEpisodeTitle> nonParsedEpisodeTitles = anime.episodes.stream()
                .map(e -> e.titles)
                .flatMap(List::stream)
                .filter(t -> isEmpty(t.lang) || isEmpty(t.text))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedEpisodeTitles, "Anime episode titles should be parsed");
    }
}