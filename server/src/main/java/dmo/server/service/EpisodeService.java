package dmo.server.service;

import dmo.server.domain.Anime;
import dmo.server.domain.Episode;
import dmo.server.repository.AnimeRepository;
import dmo.server.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EpisodeService {
    private final AnimeRepository animeRepository;
    private final EpisodeRepository episodeRepository;

    public Page<Episode> findAll(Pageable pageable, Long animeId) {
        var anime = animeRepository.findById(animeId);
        if (anime.isEmpty()) {
            return null;
        }

        return episodeRepository.findAllByAnime(pageable, anime.get());
    }
}
