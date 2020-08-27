package dmo.server.integration.anidb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnidbTracking {
    private final AnidbClient anidbClient;

    private final AtomicReference<Instant> lastUpdateInstant = new AtomicReference<>(Instant.EPOCH);

    @Scheduled(initialDelay = 10_000L, fixedDelay = 600_000L)
    public void update() throws IOException {
        Response<ResponseBody> response = anidbClient.getAnimeList().execute();
        if (!response.isSuccessful()) {
            log.warn("Failed to get AnimeList: code: {}, message: {}", response.code(), response.message());
            return;
        }

        log.info("Downloaded: {} bytes", response.body().contentLength());

        Path dst = Files.createTempFile("anime_titles", "xml.gz");
        Files.copy(response.body().byteStream(), dst, StandardCopyOption.REPLACE_EXISTING);
    }
}
