package dmo.server.integration.anidb;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.util.ObjectUtils.isEmpty;

class AnidbClientTest {

    @Test
    void getAnimeList() throws Exception {
        try (InputStream input = AnidbUpdateTrackerTest.class.getResourceAsStream("anime-titles.xml.gz")) {
            AnidbConfig anidbConfig = new AnidbConfigMock("application/gzip", input);

            AnidbClient anidbClient = anidbConfig.anidbClient();
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
            assertEquals("Anime should be parsed", Collections.emptyList(), nonParsedAnimeList);

            List<AnidbAnimeTitle> nonParsedAnimeTitleList = animeList.stream()
                    .map(a -> a.titles)
                    .flatMap(List::stream)
                    .filter(t -> isEmpty(t.type) || isEmpty(t.lang) || isEmpty(t.text))
                    .collect(Collectors.toList());
            assertEquals("Anime titles should be parsed", Collections.emptyList(), nonParsedAnimeTitleList);
        }
    }

    @Test
    void getAnime() throws Exception {
        try (InputStream input = AnidbUpdateTrackerTest.class.getResourceAsStream("anime-979.xml")) {
            AnidbConfig anidbConfig = new AnidbConfigMock("application/gzip", input);

            AnidbClient anidbClient = anidbConfig.anidbClient();
            AnidbAnime anime = anidbClient.getAnime(979L, "mock", "mock").execute().body();

            assertNotNull(anime);
            assertEquals((Long) 979L, anime.id);
            assertEquals(LocalDate.of(2003, 10, 4), anime.startDate);
            assertEquals(LocalDate.of(2004, 10, 2), anime.endDate);
            assertEquals((Long) 51L, anime.episodeCount);
            assertEquals(56, anime.titles.size());

            validateTitlesAndEpisodes(anime);
        }
    }

    @Test
    void getAnimeStrangeEpisodeNumbers() throws Exception {
        try (InputStream input = AnidbUpdateTrackerTest.class.getResourceAsStream("anime-11681.xml")) {
            AnidbConfig anidbConfig = new AnidbConfigMock("application/gzip", input);

            AnidbClient anidbClient = anidbConfig.anidbClient();
            AnidbAnime anime = anidbClient.getAnime(11681L, "mock", "mock").execute().body();

            assertNotNull(anime);
            assertEquals((Long) 11681L, anime.id);
            assertEquals(LocalDate.of(2017, 2, 18), anime.startDate);
            assertEquals(LocalDate.of(2017, 2, 18), anime.endDate);
            assertEquals((Long) 1L, anime.episodeCount);
            assertEquals(10, anime.titles.size());

            validateTitlesAndEpisodes(anime);
        }
    }

    private static void validateTitlesAndEpisodes(AnidbAnime anime) {
        List<AnidbAnimeTitle> nonParsedTitles = anime.titles.stream()
                .filter(t -> isEmpty(t.type) || isEmpty(t.lang) || isEmpty(t.text))
                .collect(Collectors.toList());
        assertEquals("Anime titles should be parsed", Collections.emptyList(), nonParsedTitles);

        List<AnidbEpisode> nonParsedEpisodes = anime.episodes.stream()
                .filter(e -> isEmpty(e.id) || isEmpty(e.length) || isEmpty(e.epno) || isEmpty(e.update)
                    || isEmpty(e.epno.getNumber()) || isEmpty(e.epno.getType()))
                .collect(Collectors.toList());
        assertEquals("Anime episodes should be parsed", Collections.emptyList(), nonParsedEpisodes);

        List<AnidbEpisodeTitle> nonParsedEpisodeTitles = anime.episodes.stream()
                .map(e -> e.titles)
                .flatMap(List::stream)
                .filter(t -> isEmpty(t.lang) || isEmpty(t.text))
                .collect(Collectors.toList());
        assertEquals("Anime episode titles should be parsed", Collections.emptyList(), nonParsedEpisodeTitles);
    }
}