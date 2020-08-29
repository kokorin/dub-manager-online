package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.event.AnimeListUpdated;
import dmo.server.event.AnimeUpdateScheduled;
import dmo.server.event.AnimeUpdated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnidbAnimeUpdater {
    private final AnidbProperties anidbProperties;
    private final AnidbClient anidbClient;
    private final ApplicationEventPublisher publisher;
    private final AnidbAnimeMapper anidbAnimeMapper;

    private final Queue<Anime> updateQueue = new PriorityBlockingQueue<>(11, ANIME_COMPARATOR);

    private final AtomicReference<Instant> lastUpdateInstant = new AtomicReference<>(Instant.EPOCH);

    private final static Duration UPDATE_TIMEOUT = Duration.ofHours(12);
    private static final Comparator<Anime> ANIME_COMPARATOR = Comparator.comparing(Anime::getId).reversed();

    @Scheduled(initialDelay = 10_000L, fixedDelay = 600_000L)
    public void updateAnimeList() throws IOException {
        if (lastUpdateInstant.get().isAfter(Instant.now())) {
            log.info("Update isn't required yet");
            return;
        }

        Response<AnidbAnimeLightList> response = anidbClient.getAnimeList().execute();
        if (!response.isSuccessful()) {
            log.warn("Failed to get AnimeList: code: {}, message: {}", response.code(), response.message());
            return;
        }

        List<AnidbAnimeLight> result = Optional.of(response)
                .map(Response::body)
                .map(x -> x.animeList)
                .orElse(Collections.emptyList());

        log.info("Update successful, got: {} items", result.size());

        List<Anime> animeList = anidbAnimeMapper.toAnimeList(result);
        AnimeListUpdated animeListUpdated = new AnimeListUpdated(animeList);
        publisher.publishEvent(animeListUpdated);

        lastUpdateInstant.set(Instant.now().plus(UPDATE_TIMEOUT));
    }

    @EventListener
    public void onAnimeUpdateScheduled(AnimeUpdateScheduled event) {
        var anime = event.getAnime();
        updateQueue.offer(anime);
        log.info("Anime update scheduled: {}", anime);
    }

    @Scheduled(initialDelay = 10_000L, fixedDelay = 2_000L)
    public void updateAnime() {
        var anime = updateQueue.poll();
        if (anime == null) {
            return;
        }

        try {
            var call = anidbClient.getAnime(anime.getId(), anidbProperties.getClient(), anidbProperties.getClientVersion());
            var response = call.execute();
            if (!response.isSuccessful()) {
                log.warn("Failed to get anime: {}, code:{}, message: {}", anime, response.code(), response.message());
                return;
            }

            var anidbAnime = response.body();
            if (anidbAnime == null || anidbAnime.id == null) {
                log.warn("Failed to get anime: {}, got: {}", anime, anidbAnime);
                return;
            }

            var updated = anidbAnimeMapper.toAnime(anidbAnime);
            var episodes = anidbAnimeMapper.toEpisodeList(anidbAnime.episodes);
            var event = new AnimeUpdated(updated, episodes);
            publisher.publishEvent(event);

            // remove instances of the same anime from the queue
            updateQueue.removeIf(a -> a.getId().equals(anime.getId()));
        } catch (Exception e) {
            log.warn("Failed to update anime: {}", anime, e);
        }
    }
}
