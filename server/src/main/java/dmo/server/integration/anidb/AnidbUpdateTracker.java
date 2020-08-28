package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.event.AnimeListUpdated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnidbUpdateTracker {
    private final AnidbClient anidbClient;
    private final ApplicationEventPublisher publisher;
    private final AnidbAnimeLightMapper anidbAnimeLightMapper;

    private final AtomicReference<Instant> lastUpdateInstant = new AtomicReference<>(Instant.EPOCH);
    private final static Duration UPDATE_TIMEOUT = Duration.ofHours(12);

    @Scheduled(initialDelay = 10_000L, fixedDelay = 600_000L)
    public void update() throws IOException {
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

        List<Anime> animeList = anidbAnimeLightMapper.toAnimeList(result);
        AnimeListUpdated animeListUpdated = new AnimeListUpdated(animeList);
        publisher.publishEvent(animeListUpdated);

        lastUpdateInstant.set(Instant.now().plus(UPDATE_TIMEOUT));
    }
}
