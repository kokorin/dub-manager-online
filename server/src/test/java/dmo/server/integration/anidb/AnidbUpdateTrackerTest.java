package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.event.AnimeListUpdated;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
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
            AnidbConfig anidbConfig = new AnidbConfigMock("application/gzip", input);

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

        List<Anime> nonParsedAnimeList = animeListUpdated.getAnimeList().stream()
                .filter(a -> a.getId() == null || CollectionUtils.isEmpty(a.getTitles()))
                .collect(Collectors.toList());
        Assert.assertEquals("Anime should be parsed", Collections.emptyList(), nonParsedAnimeList);

        List<AnimeTitle> nonParsedAnimeTitleList = animeListUpdated.getAnimeList().stream()
                .map(Anime::getTitles)
                .flatMap(List::stream)
                .filter(t -> t.getType() == null || StringUtils.isEmpty(t.getLang()) || StringUtils.isEmpty(t.getText()))
                .collect(Collectors.toList());
        Assert.assertEquals("Anime titles should be parsed", Collections.emptyList(), nonParsedAnimeTitleList);

    }

}