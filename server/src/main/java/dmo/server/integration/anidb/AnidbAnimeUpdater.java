package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.event.*;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    private final AtomicBoolean apiClientBanned = new AtomicBoolean();
    private final AtomicInteger apiClientBannedTimes = new AtomicInteger();
    private final AtomicReference<LocalDateTime> apiClientBanExpiresAfter = new AtomicReference<>(LocalDateTime.MIN);

    private final Queue<Anime> updateQueue = new PriorityBlockingQueue<>(11, ANIME_COMPARATOR);
    private static final Comparator<Anime> ANIME_COMPARATOR = Comparator.comparing(Anime::getId).reversed();
    private static final Duration BAN_BACKOFF = Duration.ofMinutes(15);

    private static final String BANNED_ERROR_MESSAGE = "banned";
    private static final String NOT_FOUND_ERROR_MESSAGE = "Anime not found";


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

    @Scheduled(initialDelay = 10_000L, fixedDelay = 5_000L)
    public void scheduledUpdateAnime() {
        var anime = updateQueue.poll();
        if (anime == null) {
            return;
        }

        if (apiClientBanned.get() && LocalDateTime.now().isBefore(apiClientBanExpiresAfter.get())) {
            log.info("Anidb banned client, expires: {}", apiClientBanExpiresAfter.get());
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

            if (apiResponse instanceof AnidbError) {
                log.warn("Failed to get anime: {}, got: {}", anime, apiResponse);
                var error = (AnidbError) apiResponse;
                switch (error.message) {
                    case BANNED_ERROR_MESSAGE:
                        apiClientBanned.set(true);
                        var times = apiClientBannedTimes.getAndIncrement();
                        var multiplier = (long) Math.pow(2, times);
                        var expires = LocalDateTime.now().plus(BAN_BACKOFF.multipliedBy(multiplier));
                        apiClientBanExpiresAfter.set(expires);
                        break;
                    case NOT_FOUND_ERROR_MESSAGE:
                        var event = new AnimeDeleted(anime.getId());
                        publisher.publishEvent(event);
                        break;
                    default:
                        break;
                }
                return;
            }

            if (!(apiResponse instanceof AnidbAnime)) {
                return;
            }

            var anidbAnime = (AnidbAnime) apiResponse;

            var updated = anidbAnimeMapper.toAnime(anidbAnime);
            var episodes = anidbAnimeMapper.toEpisodeList(anidbAnime.episodes);
            var event = new AnimeUpdated(updated, episodes);
            publisher.publishEvent(event);

            // remove instances of the same anime from the queue
            updateQueue.removeIf(a -> a.getId().equals(anime.getId()));

            // clean up banned status
            apiClientBanned.set(false);
            apiClientBannedTimes.set(0);
        } catch (Exception e) {
            log.warn("Failed to update anime: {}", anime, e);
        }
    }
}
