package dmo.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dmo.server.domain.Anime;
import dmo.server.repository.AnimeRepository;

@Service
public class AnimeService {
    @Autowired
    private AnimeRepository animeRepository;

    public Page<Anime> findAll(Pageable page) {
        return animeRepository.findAll(page);
    }


}