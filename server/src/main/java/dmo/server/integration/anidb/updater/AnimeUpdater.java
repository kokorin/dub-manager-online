package dmo.server.integration.anidb.updater;

import dmo.server.domain.Anime;
import dmo.server.domain.ExternalSystem;
import dmo.server.event.*;
import dmo.server.integration.anidb.client.AnidbClient;
import dmo.server.integration.anidb.mapper.AnidbAnimeMapper;
import dmo.server.integration.anidb.prop.AnidbApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnimeUpdater {
    private final AnidbApiProperties anidbProperties;
    private final AnidbClient anidbClient;
    private final ApplicationEventPublisher publisher;
    private final AnidbAnimeMapper anidbAnimeMapper;
    private final AnidbApiProperties apiProperties;

    private final AtomicBoolean apiClientBanned = new AtomicBoolean();
    private final AtomicInteger apiClientBannedTimes = new AtomicInteger();
    private final AtomicReference<LocalDateTime> apiClientBanExpiresAfter = new AtomicReference<>(LocalDateTime.MIN);

    private static final Duration BAN_BACKOFF = Duration.ofHours(6);

    private static final String BANNED_ERROR_MESSAGE = "banned";
    private static final String NOT_FOUND_ERROR_MESSAGE = "Anime not found";

    @Async
    @EventListener
    public void onAnimeRequested(AnimeRequested event) {
        var anime = event.getAnime();

        if (!anime.externalSystem().equals(ExternalSystem.ANIDB)) {
            log.debug("Not ANIDB: {}", anime);
            return;
        }

        // TODO improve update strategy to update old anime rarely (not once per day)
        // May be we can target anime episode air date to schedule smart updates
        if (anime.lastUpdate().plus(apiProperties.updateInterval).isAfter(Instant.now())) {
            log.debug("Too early for update: {}", anime);
            return;
        }

        log.info("Anime update scheduled: {}", anime);
        updateAnime(anime);
    }

    public void updateAnime(Anime anime) {
        if (apiClientBanned.get() && LocalDateTime.now().isBefore(apiClientBanExpiresAfter.get())) {
            log.info("Anidb banned client, expires: {}", apiClientBanExpiresAfter.get());
            return;
        }

        try {
            log.info("Will update anime: {}", anime);

            var call = anidbClient.getAnime(anime.id(),
                    anidbProperties.clientName, anidbProperties.clientVersion);
            var response = call.execute();
            if (!response.isSuccessful()) {
                log.warn("Failed to get anime: {}, code:{}, message: {}", anime, response.code(), response.message());
                switch (response.message()) {
                    case BANNED_ERROR_MESSAGE -> {
                        apiClientBanned.set(true);
                        var times = apiClientBannedTimes.getAndIncrement();
                        var multiplier = (long) Math.pow(2, times);
                        var expires = LocalDateTime.now().plus(BAN_BACKOFF.multipliedBy(multiplier));
                        apiClientBanExpiresAfter.set(expires);
                    }
                    case NOT_FOUND_ERROR_MESSAGE -> {
                        var event = new AnimeDeleted(anime.id());
                        publisher.publishEvent(event);
                    }
                    default -> {
                        log.warn("API error ignored: {}", response.message());
                    }
                }
                return;
            }

            var anidbAnime = response.body();

            var updated = anidbAnimeMapper.toAnime(anidbAnime);
            var episodes = anidbAnimeMapper.toEpisodeList(anidbAnime.episodes, updated.id());
            var event = new AnimeUpdated(updated, episodes);
            publisher.publishEvent(event);

            // clean up banned status
            apiClientBanned.set(false);
            apiClientBannedTimes.set(0);
        } catch (Exception e) {
            log.warn("Failed to update anime: {}", anime, e);
        }
    }
}
