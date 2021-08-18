package dmo.server.service;

import dmo.server.domain.AnimeStatus;
import dmo.server.domain.User;
import dmo.server.event.AnimeRequested;
import dmo.server.exception.AnimeNotFoundException;
import dmo.server.exception.AnimeStatusNotFoundException;
import dmo.server.exception.UserNotFoundException;
import dmo.server.repository.AnimeRepository;
import dmo.server.repository.AnimeStatusRepository;
import dmo.server.repository.EpisodeStatusRepository;
import dmo.server.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimeStatusService {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final AnimeRepository animeRepository;
    @NonNull
    private final AnimeStatusRepository animeStatusRepository;
    @NonNull
    private final EpisodeStatusRepository episodeStatusRepository;
    @NonNull
    private final ApplicationEventPublisher eventPublisher;

    @Secured("ROLE_USER")
    @Transactional
    public Page<AnimeStatus> findAll(String userEmail, Pageable pageable) {
        User user = userRepository.findById(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        return animeStatusRepository.findAllByUser(user, pageable);
    }

    @Secured("ROLE_USER")
    @Transactional
    public AnimeStatus updateAnimeStatus(String userEmail, Long animeId, Consumer<AnimeStatus> updater) {
        var user = userRepository.findById(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        var anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException(animeId));

        eventPublisher.publishEvent(new AnimeRequested(anime));

        var updateEpisodeCount = episodeStatusRepository.fillEpisodeStatusesForUser(anime, user);

        var animeStatus = animeStatusRepository.findByUserAndAnime(user, anime)
                .orElseGet(() -> {
                    var result = new AnimeStatus();
                    result.setUser(user);
                    result.setAnime(anime);
                    result.setRegularEpisodeCompleteCount(0L);
                    result.setRegularEpisodeTotalCount(Optional.ofNullable(anime.getEpisodeCount()).orElse(0L));
                    result.setRegularEpisodeNextAirDate(
                            episodeStatusRepository.getRegularEpisodeNextAirDate(anime, user)
                    );
                    result.setProgress(AnimeStatus.Progress.NOT_STARTED);
                    // If AnimeStatus was created we have to save it
                    return animeStatusRepository.save(result);
                });

        updater.accept(animeStatus);

        log.info("Updated anime status {} for {}: episodes {}", anime, user, updateEpisodeCount);

        return animeStatus;
    }

    @Secured("ROLE_USER")
    @Transactional
    public void deleteAnimeStatus(String userEmail, Long animeId) {
        var id = new AnimeStatus.AnimeStatusId(animeId, userEmail);
        var animeStatus = animeStatusRepository.findById(id)
                .orElseThrow(() -> new AnimeStatusNotFoundException(id));

        episodeStatusRepository.deleteAllByAnimeAndUser(animeId, userEmail);
        animeStatusRepository.delete(animeStatus);
    }
}
