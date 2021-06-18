package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.event.AnimeListUpdateScheduled;
import dmo.server.event.AnimeListUpdated;
import dmo.server.event.AnimeUpdateScheduled;
import dmo.server.event.AnimeUpdated;
import dmo.server.prop.AnidbProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnidbAnimeUpdater {
    @NonNull
    private final AnidbProperties anidbProperties;
    @NonNull
    private final AnidbClient anidbClient;
    @NonNull
    private final ApplicationEventPublisher publisher;
    @NonNull
    private final AnidbAnimeMapper anidbAnimeMapper;

    private final Queue<Anime> updateQueue = new PriorityBlockingQueue<>(11, ANIME_COMPARATOR);
    private static final Comparator<Anime> ANIME_COMPARATOR = Comparator.comparing(Anime::getId).reversed();

    @EventListener
    public void onUpdateAnimeListScheduled(AnimeListUpdateScheduled event) throws IOException {
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
    }

    @EventListener
    public void onAnimeUpdateScheduled(AnimeUpdateScheduled event) {
        var anime = event.getAnime();
        updateQueue.offer(anime);
        log.info("Anime update scheduled: {}", anime);
    }

    @Scheduled(initialDelay = 10_000L, fixedDelay = 3_000L)
    public void updateAnime() {
        var anime = updateQueue.poll();
        if (anime == null) {
            return;
        }

        try {
            log.info("Will update anime: {}", anime);

            var call = anidbClient.getAnime(anime.getId(),
                    anidbProperties.client, anidbProperties.clientVersion);
            var response = call.execute();
            if (!response.isSuccessful()) {
                log.warn("Failed to get anime: {}, code:{}, message: {}", anime, response.code(), response.message());
                return;
            }

            var apiResponse = response.body();

            if (!(apiResponse instanceof AnidbAnime)) {
                log.warn("Failed to get anime: {}, got: {}", anime, apiResponse);
                return;
            }

            var anidbAnime = (AnidbAnime) apiResponse;

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
