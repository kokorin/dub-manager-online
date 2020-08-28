package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.event.AnimeListUpdated;
import dmo.server.okhttp.MockInterceptor;
import okhttp3.Interceptor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class AnidbUpdateTrackerTest {
    private AnidbAnimeLightMapper anidbAnimeLightMapper = Mappers.getMapper(AnidbAnimeLightMapper.class);

    @Test
    void testUpdate() throws Exception {
        AtomicReference<Object> eventRef = new AtomicReference<>();
        ApplicationEventPublisher eventPublisher = new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
                eventRef.set(event);
            }
        };

        try (InputStream input = AnidbUpdateTrackerTest.class.getResourceAsStream("anime-titles.xml.gz")) {
            AnidbConfig anidbConfig = new AnidbConfig() {
                @Override
                List<Interceptor> interceptors() {
                    List<Interceptor> result = new ArrayList<>(super.interceptors());
                    result.add(new MockInterceptor("application/gzip", input));
                    return result;
                }
            };

            AnidbClient anidbClient = anidbConfig.anidbClient();
            AnidbUpdateTracker tracking = new AnidbUpdateTracker(anidbClient, eventPublisher, anidbAnimeLightMapper);
            tracking.update();
        }

        Object event = eventRef.get();
        Assert.assertNotNull(event);
        Assert.assertTrue(event instanceof AnimeListUpdated);

        AnimeListUpdated animeListUpdated = (AnimeListUpdated) event;
        Assert.assertEquals(4, animeListUpdated.getAnimeList().size());

        Anime anime = animeListUpdated.getAnimeList().get(0);
        Assert.assertEquals(Long.valueOf(1L), anime.getId());
        Assert.assertEquals(10, anime.getTitles().size());

        AnimeTitle title = anime.getTitles().get(0);
        Assert.assertEquals(AnimeTitle.Type.SHORT, title.getType());
        Assert.assertEquals("en", title.getLang());
        Assert.assertEquals("CotS", title.getText());

        long emptyTitlesCount = animeListUpdated.getAnimeList().stream()
                .map(Anime::getTitles)
                .filter(CollectionUtils::isEmpty)
                .count();

        Assert.assertEquals("Anime titles shouldn't be empty", 0L, emptyTitlesCount);

        List<Anime> nonParsedAnimeList = animeListUpdated.getAnimeList().stream()
                .filter(a -> a.getId() == null || a.getTitles() == null || a.getType() == null)
                .collect(Collectors.toList());
        Assert.assertEquals("Anime should be parsed", Collections.emptyList(), nonParsedAnimeList);

    }

}