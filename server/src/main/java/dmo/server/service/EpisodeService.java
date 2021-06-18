package dmo.server.service;

import dmo.server.domain.Episode;
import dmo.server.exception.AnimeNotFoundException;
import dmo.server.repository.AnimeRepository;
import dmo.server.repository.EpisodeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EpisodeService {
    @NonNull
    private final AnimeRepository animeRepository;
    @NonNull
    private final EpisodeRepository episodeRepository;

    @Secured("ROLE_USER")
    public Page<Episode> findAll(Pageable pageable, Long animeId) {
        var anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException(animeId));

        return episodeRepository.findAllByAnime(pageable, anime);
    }
}
