package dmo.server.service;

import dmo.server.domain.AnimeStatus;
import dmo.server.domain.EpisodeStatus;
import dmo.server.exception.AnimeStatusNotFoundException;
import dmo.server.exception.EpisodeStatusNotFoundException;
import dmo.server.repository.AnimeStatusRepository;
import dmo.server.repository.EpisodeStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class EpisodeStatusService {
    private final AnimeStatusRepository animeStatusRepository;
    private final EpisodeStatusRepository episodeStatusRepository;

    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    public Page<EpisodeStatus> findByAnimeAndUser(Long animeId, String userEmail, PageRequest pageRequest) {
        var id = new AnimeStatus.AnimeStatusId(animeId, userEmail);
        var animeStatus = animeStatusRepository.findById(id)
                .orElseThrow(() -> new AnimeStatusNotFoundException(id));

        pageRequest = pageRequest.withSort(Sort.by("episode.type", "episode.number"));
        return episodeStatusRepository.findAllByAnimeAndUser(animeStatus.getAnime(), animeStatus.getUser(), pageRequest);
    }

    @Secured("ROLE_USER")
    @Transactional
    public EpisodeStatus updateEpisodeStatus(Long animeId, Long episodeId, String userEmail, Consumer<EpisodeStatus> updater) {
        var animeStatusId = new AnimeStatus.AnimeStatusId(animeId, userEmail);
        var animeStatus = animeStatusRepository.findById(animeStatusId)
                .orElseThrow(() -> new AnimeStatusNotFoundException(animeStatusId));

        var episodeStatusId = new EpisodeStatus.EpisodeStatusId(episodeId, userEmail);
        var episodeStatus = episodeStatusRepository.findById(episodeStatusId)
                .filter(s -> s.getEpisode().getAnime().getId().equals(animeId))
                .orElseThrow(() -> new EpisodeStatusNotFoundException(episodeStatusId));

        updater.accept(episodeStatus);
        episodeStatusRepository.flush();

        var completeCount = episodeStatusRepository.getRegularEpisodeCompleteCount(
                animeStatus.getAnime(),
                animeStatus.getUser()
        );
        animeStatus.setRegularEpisodeCompleteCount(completeCount);

        var airDate = episodeStatusRepository.getRegularEpisodeNextAirDate(
                animeStatus.getAnime(),
                animeStatus.getUser()
        );
        animeStatus.setRegularEpisodeNextAirDate(airDate);

        log.debug("Updated EpisodeStatus: {}", episodeStatus);

        return episodeStatus;
    }
}
