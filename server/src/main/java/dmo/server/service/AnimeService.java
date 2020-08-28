package dmo.server.service;

import dmo.server.domain.Anime;
import dmo.server.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;

    public Page<Anime> findAll(Pageable page) {
        return animeRepository.findAll(page);
    }

    @Transactional
    public void saveAll(List<Anime> animeList) {
        animeRepository.saveAll(animeList);
    }
}