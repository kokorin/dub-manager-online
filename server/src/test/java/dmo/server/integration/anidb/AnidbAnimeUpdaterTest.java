package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.domain.Episode;
import dmo.server.event.*;
import dmo.server.prop.AnidbProperties;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.CollectionUtils;
import retrofit2.mock.Calls;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@SpringBootTest(
        classes = {AnidbConfig.class, MockAnidbConf.class},
        properties = "spring.main.allow-bean-definition-overriding=true")
public class AnidbAnimeUpdaterTest {
    private final AnidbProperties anidbProperties = new AnidbProperties("", "");

    @Autowired
    private MockResponseInterceptor mockResponseInterceptor;

    private final AnidbAnimeMapper anidbAnimeMapper = Mappers.getMapper(AnidbAnimeMapper.class);

    @Autowired
    private AnidbClient anidbClient;

    @Test
    void updateAnimeList() throws Exception {
        AtomicReference<Object> eventRef = new AtomicReference<>();
        ApplicationEventPublisher eventPublisher = new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
                eventRef.set(event);
            }
        };

        var anidbAnimeUpdater = new AnidbAnimeUpdater(anidbProperties, anidbClient, eventPublisher, anidbAnimeMapper);
        anidbAnimeUpdater.onUpdateAnimeListScheduled(new AnimeListUpdateScheduled());

        Object event = eventRef.get();
        assertNotNull(event);
        assertTrue(event instanceof AnimeListUpdated);

        AnimeListUpdated animeListUpdated = (AnimeListUpdated) event;
        assertEquals(6, animeListUpdated.getAnimeList().size());

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

        var anidbAnimeUpdater = new AnidbAnimeUpdater(anidbProperties, anidbClient, eventPublisher, anidbAnimeMapper);

        var anime = new Anime();
        anime.setId(979L);
        anidbAnimeUpdater.onAnimeUpdateScheduled(new AnimeUpdateScheduled(anime));
        anidbAnimeUpdater.scheduledUpdateAnime();

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

    @Test
    public void animeIsNotRequestedIfBanned() throws InterruptedException {
        mockResponseInterceptor.useResponseFileOnce("api-banned.xml");

        var eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        var anidbAnimeUpdater = new AnidbAnimeUpdater(anidbProperties, anidbClient, eventPublisher, anidbAnimeMapper);

        anidbAnimeUpdater.onAnimeUpdateScheduled(animeUpdateScheduled(47L));
        // should return banned
        anidbAnimeUpdater.scheduledUpdateAnime();

        // Should not call API
        TimeUnit.SECONDS.sleep(1);
        anidbAnimeUpdater.onAnimeUpdateScheduled(animeUpdateScheduled(48L));
        anidbAnimeUpdater.scheduledUpdateAnime();
        anidbAnimeUpdater.onAnimeUpdateScheduled(animeUpdateScheduled(49L));
        anidbAnimeUpdater.scheduledUpdateAnime();
        anidbAnimeUpdater.onAnimeUpdateScheduled(animeUpdateScheduled(50L));
        anidbAnimeUpdater.scheduledUpdateAnime();
    }

    @Test
    public void animeDeletedEventIsSentAfterNotFoundAnimeResponse() throws InterruptedException {
        final List<Object> events = new CopyOnWriteArrayList<>();
        ApplicationEventPublisher eventPublisher = new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
                events.add(event);
            }
        };

        var notFound = new AnidbError();
        notFound.code = 500;
        notFound.message = "Anime not found";

        var anidbClient = Mockito.mock(AnidbClient.class);
        Mockito.when(anidbClient.getAnime(47L, "", ""))
                .thenReturn(Calls.response(notFound))
                .thenThrow(RuntimeException.class);

        var anidbAnimeUpdater = new AnidbAnimeUpdater(anidbProperties, anidbClient, eventPublisher, anidbAnimeMapper);

        anidbAnimeUpdater.onAnimeUpdateScheduled(animeUpdateScheduled(47L));
        // should return anime with DELETED status
        anidbAnimeUpdater.scheduledUpdateAnime();

        assertEquals(1, events.size());

        Object eventObj = events.get(0);
        assertNotNull(eventObj);
        assertThat(eventObj, isA(AnimeDeleted.class));

        AnimeDeleted deleted = (AnimeDeleted) eventObj;
        assertEquals((Long) 47L, deleted.getAnimeId());
    }

    private static AnimeUpdateScheduled animeUpdateScheduled(Long animeId) {
        var anime = new Anime();
        anime.setId(animeId);
        return new AnimeUpdateScheduled(anime);
    }
}