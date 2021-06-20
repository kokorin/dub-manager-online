package dmo.server.service;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeUpdate;
import dmo.server.event.*;
import dmo.server.repository.AnimeRepository;
import dmo.server.repository.AnimeUpdateRepository;
import dmo.server.repository.EpisodeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnimeUpdater {
    @NonNull
    private final AnimeRepository animeRepository;
    @NonNull
    private final EpisodeRepository episodeRepository;
    @NonNull
    private final AnimeUpdateRepository animeUpdateRepository;
    @NonNull
    private final ApplicationEventPublisher eventPublisher;

    private final AtomicReference<Instant> lastUpdateInstant = new AtomicReference<>(Instant.now());

    private final static Duration ANIME_UPDATE_PERIOD = Duration.ofDays(1);
    private final static Duration ANIME_LIST_UPDATE_PERIOD = Duration.ofDays(1);

    @Scheduled(initialDelay = 10_000L, fixedDelay = 600_000L)
    public void scheduleAnimeListUpdate() {
        long animeCount = animeRepository.count();
        boolean timeToUpdate = lastUpdateInstant.get().isBefore(Instant.now().minus(ANIME_LIST_UPDATE_PERIOD));

        if (animeCount == 0 || timeToUpdate) {
            log.info("Anime list update scheduled");
            // TODO add AnimeListUpdateSuccess event
            eventPublisher.publishEvent(new AnimeListUpdateScheduled());
            lastUpdateInstant.set(Instant.now());
        }
    }

    @Scheduled(initialDelay = 20_000L, fixedDelay = 60_000L)
    public void scheduleAnimeUpdate() {
        var anime = animeRepository.findAnimeWithoutEpisodes();
        var updateScheduled = new AnimeUpdateScheduled(anime);
        eventPublisher.publishEvent(updateScheduled);
        log.info("Scheduled Anime update: {}", anime.getId());
    }

    @EventListener
    @Transactional
    public void onAnimeListUpdate(AnimeListUpdated animeListUpdated) {
        log.info("Received Anime List update, persisting...");
        List<Anime> animeList = animeListUpdated.getAnimeList();
        List<Anime> persistedList = animeRepository.findAllWithTitles();

        Map<Long, Anime> persistedById = persistedList.stream()
                .collect(Collectors.toMap(Anime::getId, Function.identity()));

        long updated = 0;
        long inserted = 0;

        for (Anime anime : animeList) {
            Anime persisted = persistedById.get(anime.getId());
            if (persisted != null) {
                if (!Objects.equals(persisted.getTitles(), anime.getTitles())) {
                    persisted.setTitles(anime.getTitles());
                    updated++;
                }
            } else {
                anime.setType(Anime.Type.UNKNOWN);
                animeRepository.save(anime);
                inserted++;
            }
        }

        log.info("Got: {}, inserted: {}, updated: {}", animeList.size(), inserted, updated);
    }

    @Async
    @EventListener
    public void onAnimeRequested(AnimeRequested event) {
        var anime = event.getAnime();
        Instant animeLastUpdated = animeUpdateRepository.findByAnime(anime)
                .map(AnimeUpdate::getLastUpdated)
                .orElse(Instant.EPOCH);

        // TODO improve update strategy to update old anime rarely (not once per day)
        // May be we can target anime episode air date to schedule smart updates
        if (animeLastUpdated.isBefore(Instant.now().minus(ANIME_UPDATE_PERIOD))) {
            log.info("Anime update scheduled: {}", anime);
            var updateScheduled = new AnimeUpdateScheduled(anime);
            eventPublisher.publishEvent(updateScheduled);
        }
    }

    @EventListener
    @Transactional
    public void onAnimeUpdated(AnimeUpdated event) {
        var anime = event.getAnime();
        var episodes = event.getEpisodes();

        var savedAnime = animeRepository.save(anime);

        if (episodes != null) {
            episodes.forEach(e -> e.setAnime(savedAnime));
            episodeRepository.saveAll(episodes);
        }

        var animeUpdate = new AnimeUpdate();
        // No need to set animeUpdate.id
        animeUpdate.setAnime(savedAnime);
        animeUpdate.setLastUpdated(Instant.now());
        animeUpdateRepository.save(animeUpdate);
        log.info("Anime {} updated", anime.getId());
    }
}
