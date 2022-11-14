package dmo.server.integration.anidb.updater;

import dmo.server.domain.Anime;
import dmo.server.domain.ExternalSystem;
import dmo.server.integration.anidb.client.AnidbClient;
import dmo.server.integration.anidb.mapper.AnidbAnimeMapper;
import dmo.server.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Component
@Slf4j
public class AnimeTitlesUpdater {
    private final AnidbClient client;
    private final AnidbAnimeMapper mapper;
    private final AnimeRepository repository;
    private final Duration titlesUpdatePeriod;
    private volatile Instant nextUpdateAfter = Instant.now();

    public AnimeTitlesUpdater(
            AnidbClient client,
            AnidbAnimeMapper mapper,
            AnimeRepository repository,
            @Value("${integration.anidb.titles_update_period}")
            Duration titlesUpdatePeriod
    ) {
        this.client = client;
        this.mapper = mapper;
        this.repository = repository;
        this.titlesUpdatePeriod = titlesUpdatePeriod;
    }

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

        var persistedAnimeByExternalId = repository.findAllByExternalSystem(ExternalSystem.ANIDB)
                .stream()
                .collect(toMap(Anime::externalId, identity()));

        var animeTitlesList = Optional.ofNullable(response.body())
                .map(al -> al.animeList)
                .orElse(Collections.emptyList());

        long updateCount = 0;
        long insertCount = 0;

        List<Anime> toUpdate = new ArrayList<>();

        // For each Anime retrieved from AniDB check if we need to update it
        for (var animeTitles : animeTitlesList) {
            var titles = mapper.toAnimeTitles(animeTitles.titles);
            Anime persisted = persistedAnimeByExternalId.get(animeTitles.id);
            // Anime with the same externalId is already in DB
            if (persisted != null) {
                var sameTitles = Objects.equals(persisted.titles(), titles);
                // if anime appears in titles list - it is not deleted
                if (!sameTitles || persisted.deleted()) {
                    toUpdate.add(
                            persisted
                                    .deleted(false)
                                    .titles(titles)
                    );
                    updateCount++;
                }
            } else {
                // Anime not in DB
                toUpdate.add(
                        Anime.builder()
                                .externalId(animeTitles.id)
                                .externalSystem(ExternalSystem.ANIDB)
                                .titles(titles)
                                .deleted(false)
                                .type(Anime.Type.UNKNOWN)
                                .build()
                );
                insertCount++;
            }
        }


        var animeExternalIds = animeTitlesList.stream()
                .map(at -> at.id)
                .collect(toSet());
        persistedAnimeByExternalId.keySet().stream()
                .filter(id -> !animeExternalIds.contains(id))
                .forEach(deletedId ->
                        toUpdate.add(
                                persistedAnimeByExternalId.get(deletedId)
                                        .deleted(true)
                        )
                );

        repository.saveAll(toUpdate);

        log.info("Got: {}, inserted: {}, updated: {}", animeTitlesList.size(), insertCount, updateCount);

        nextUpdateAfter = nextUpdateAfter.plus(titlesUpdatePeriod);
    }
}
