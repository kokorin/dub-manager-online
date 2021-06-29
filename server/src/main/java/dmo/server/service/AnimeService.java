package dmo.server.service;

import dmo.server.domain.Anime;
import dmo.server.event.AnimeRequested;
import dmo.server.exception.AnimeNotFoundException;
import dmo.server.repository.AnimeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimeService {

    @NonNull
    private final AnimeRepository animeRepository;
    @NonNull
    private final ApplicationEventPublisher eventPublisher;

    @Secured("ROLE_USER")
    public Anime findById(Long id) {
        var result = animeRepository.findById(id)
                .orElseThrow(() -> new AnimeNotFoundException(id));

        eventPublisher.publishEvent(new AnimeRequested(result));

        return result;
    }

    @Secured("ROLE_USER")
    public Page<Anime> findAll(Pageable page) {
        return animeRepository.findAll(page);
    }

    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
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