package dmo.server.integration.anidb;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class AnidbClientTest {

    @Test
    void getAnimeList() throws Exception {
        try (InputStream input = AnidbUpdateTrackerTest.class.getResourceAsStream("anime-titles.xml.gz")) {
            AnidbConfig anidbConfig = new AnidbConfigMock("application/gzip", input);

            AnidbClient anidbClient = anidbConfig.anidbClient();
            List<AnidbAnimeLight> animeList = anidbClient.getAnimeList().execute().body().animeList;

            AnidbAnimeLight anime = animeList.get(0);
            Assert.assertEquals(Long.valueOf(1L), anime.id);
            Assert.assertEquals(10, anime.titles.size());

            AnidbAnimeTitle title = anime.titles.get(0);
            Assert.assertEquals(AnidbAnimeTitle.Type.SHORT, title.type);
            Assert.assertEquals("en", title.lang);
            Assert.assertEquals("CotS", title.text);

            List<AnidbAnimeLight> nonParsedAnimeList = animeList.stream()
                    .filter(a -> a.id == null || CollectionUtils.isEmpty(a.titles))
                    .collect(Collectors.toList());
            Assert.assertEquals("Anime should be parsed", Collections.emptyList(), nonParsedAnimeList);

            List<AnidbAnimeTitle> nonParsedAnimeTitleList = animeList.stream()
                    .map(a -> a.titles)
                    .flatMap(List::stream)
                    .filter(t -> t.type == null || StringUtils.isEmpty(t.lang) || StringUtils.isEmpty(t.text))
                    .collect(Collectors.toList());
            Assert.assertEquals("Anime titles should be parsed", Collections.emptyList(), nonParsedAnimeTitleList);
        }
    }

    @Test
    void getAnime() throws Exception {
        try (InputStream input = AnidbUpdateTrackerTest.class.getResourceAsStream("anime-979.xml")) {
            AnidbConfig anidbConfig = new AnidbConfigMock("application/gzip", input);

            AnidbClient anidbClient = anidbConfig.anidbClient();
            AnidbAnime anime = anidbClient.getAnime(979L, "mock", "mock").execute().body();
        }
    }

    @Test
    void getAnimeStrangeEpisodeNumbers() throws Exception {
        try (InputStream input = AnidbUpdateTrackerTest.class.getResourceAsStream("anime-11681.xml")) {
            AnidbConfig anidbConfig = new AnidbConfigMock("application/gzip", input);

            AnidbClient anidbClient = anidbConfig.anidbClient();
            AnidbAnime anime = anidbClient.getAnime(11681L, "mock", "mock").execute().body();
        }
    }
}