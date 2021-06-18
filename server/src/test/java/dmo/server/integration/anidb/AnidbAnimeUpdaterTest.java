package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.domain.Episode;
import dmo.server.event.AnimeListUpdateScheduled;
import dmo.server.event.AnimeListUpdated;
import dmo.server.event.AnimeUpdateScheduled;
import dmo.server.event.AnimeUpdated;
import dmo.server.prop.AnidbProperties;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.util.ObjectUtils.isEmpty;

public class AnidbAnimeUpdaterTest {
    private AnidbAnimeMapper anidbAnimeMapper = Mappers.getMapper(AnidbAnimeMapper.class);
    private AnidbProperties anidbProperties = new AnidbProperties("", "");

    @Test
    void updateAnimeList() throws Exception {
        AtomicReference<Object> eventRef = new AtomicReference<>();
        ApplicationEventPublisher eventPublisher = new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
                eventRef.set(event);
            }
        };

        try (InputStream input = AnidbAnimeUpdaterTest.class.getResourceAsStream("anime-titles.xml.gz")) {
            AnidbConfig anidbConfig = new AnidbConfigMock("application/gzip", input);

            AnidbClient anidbClient = anidbConfig.anidbClient();
            AnidbAnimeUpdater tracking = new AnidbAnimeUpdater(anidbProperties, anidbClient, eventPublisher, anidbAnimeMapper);
            tracking.onUpdateAnimeListScheduled(new AnimeListUpdateScheduled());
        }

        Object event = eventRef.get();
        assertNotNull(event);
        assertTrue(event instanceof AnimeListUpdated);

        AnimeListUpdated animeListUpdated = (AnimeListUpdated) event;
        assertEquals(4, animeListUpdated.getAnimeList().size());

        Anime anime = animeListUpdated.getAnimeList().get(0);
        assertEquals(Long.valueOf(1L), anime.getId());
        assertEquals(10, anime.getTitles().size());

        List<Anime> nonParsedAnimeList = animeListUpdated.getAnimeList().stream()
                .filter(a -> a.getId() == null || CollectionUtils.isEmpty(a.getTitles()))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedAnimeList, "Anime should be parsed");

        List<AnimeTitle> nonParsedAnimeTitleList = animeListUpdated.getAnimeList().stream()
                .map(Anime::getTitles)
                .flatMap(Collection::stream)
                .filter(t -> t.getType() == null || isEmpty(t.getLang()) || isEmpty(t.getText()))
                .collect(Collectors.toList());
        assertEquals(Collections.emptyList(), nonParsedAnimeTitleList, "Anime titles should be parsed");

    }

    @Test
    void updateAnime() throws Exception {
        AtomicReference<Object> eventRef = new AtomicReference<>();
        ApplicationEventPublisher eventPublisher = new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
                eventRef.set(event);
            }
        };

        try (InputStream input = AnidbAnimeUpdaterTest.class.getResourceAsStream("anime-979.xml")) {
            AnidbConfig anidbConfig = new AnidbConfigMock("application/xml", input);

            AnidbClient anidbClient = anidbConfig.anidbClient();
            AnidbAnimeUpdater tracking = new AnidbAnimeUpdater(anidbProperties, anidbClient, eventPublisher, anidbAnimeMapper);

            var anime = new Anime();
            anime.setId(979L);
            tracking.onAnimeUpdateScheduled(new AnimeUpdateScheduled(anime));
            tracking.updateAnime();
        }

        Object event = eventRef.get();
        assertNotNull(event);
        assertTrue(event instanceof AnimeUpdated);

        var updatedEvent = (AnimeUpdated) event;
        var updated = updatedEvent.getAnime();
        var episodes = updatedEvent.getEpisodes();

        assertNotNull(updated);
        assertEquals((Long) 979L, updated.getId());
        assertEquals(Anime.Type.TV_SERIES, updated.getType());
        assertEquals((Long) 51L, updated.getEpisodeCount());

        assertNotNull(episodes);
        assertEquals(141, episodes.size());
        var regularEpisodes = episodes.stream().filter(e -> e.getType() == Episode.Type.REGULAR).count();
        assertEquals(51, regularEpisodes);
    }
}