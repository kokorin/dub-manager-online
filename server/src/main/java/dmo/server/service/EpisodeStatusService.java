package dmo.server.service;

import dmo.server.domain.EpisodeStatus;
import dmo.server.exception.AnimeNotFoundException;
import dmo.server.exception.UserNotFoundException;
import dmo.server.repository.AnimeRepository;
import dmo.server.repository.EpisodeStatusRepository;
import dmo.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EpisodeStatusService {
    private final AnimeRepository animeRepository;
    private final UserRepository userRepository;
    private final EpisodeStatusRepository episodeStatusRepository;

    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    public Page<EpisodeStatus> findByAnimeAndUser(Long animeId, String userEmail, PageRequest pageRequest) {
        var anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException(animeId));
        var user = userRepository.findById(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        pageRequest = pageRequest.withSort(Sort.by("episode.type", "episode.number"));
        return episodeStatusRepository.findAllByAnimeAndUser(anime, user, pageRequest);
    }

    @Transactional
    public void fillEpisodeStatuses(Long animeId) {

    }
}
