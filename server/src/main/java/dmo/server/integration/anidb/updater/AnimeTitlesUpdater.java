package dmo.server.integration.anidb.updater;

import dmo.server.domain.ExternalSystem;
import dmo.server.integration.anidb.client.AnidbTitlesClient;
import dmo.server.integration.anidb.prop.AnidbTitlesProperties;
import dmo.server.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static java.util.stream.Collectors.toMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnimeTitlesUpdater {
    private final AnidbTitlesClient client;
    private final AnimeRepository repository;
    private final AnidbTitlesProperties titlesProperties;
    private final UpdateAnimeStrategy updateAnimeStrategy;

    private volatile Instant nextUpdateAfter = Instant.now();

    @Scheduled(fixedDelayString = "${integration.anidb.titles_update_delay}")
    public void updateAnimeTitle() throws IOException {
        if (Instant.now().isBefore(nextUpdateAfter)) {
            log.info("To early for update, scheduled after: {}", nextUpdateAfter);
            return;
        }

        var response = client.getAnimeList().execute();
        if (!response.isSuccessful()) {
            log.warn("Failed to get anime titles: {}", response.errorBody());
            return;
        }
        var knownAnime = repository.findAllByExternalSystem(ExternalSystem.ANIDB);

        var freshAnimeTitles = Optional.ofNullable(response.body())
                .map(al -> al.animeList)
                .orElse(Collections.emptyList());

        var toUpdate = updateAnimeStrategy.changedAnime(knownAnime, freshAnimeTitles);

        repository.saveAll(toUpdate);

        nextUpdateAfter = nextUpdateAfter.plus(titlesProperties.updateInterval);
    }
}
