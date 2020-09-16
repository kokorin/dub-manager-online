package dmo.server.service;

import dmo.server.domain.Anime;
import dmo.server.event.AnimeRequested;
import dmo.server.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Optional<Anime> findById(Long id) {
        var result = animeRepository.findById(id);

        result.map(AnimeRequested::new).ifPresent(eventPublisher::publishEvent);

        return result;
    }

    public Page<Anime> findAll(Pageable page) {
        return animeRepository.findAll(page);
    }

    @Transactional
    public Page<Anime> findByTitle(String title, Pageable page) {
        var ids = animeRepository.findIdByTitle(title, page);
        var content = animeRepository.findAllWithTitles(ids, page.getSort());
        var total = animeRepository.countByTitle(title);

        return new PageImpl<>(
                content,
                page,
                total
        );
    }
}