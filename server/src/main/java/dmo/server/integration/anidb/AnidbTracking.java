package dmo.server.integration.anidb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnidbTracking {
    private final AnidbClient anidbClient;

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

        lastUpdateInstant.set(Instant.now().plus(UPDATE_TIMEOUT));
    }
}
