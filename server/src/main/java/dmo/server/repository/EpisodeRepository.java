package dmo.server.repository;

import dmo.server.domain.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends CrudRepository<Episode, Long> {
    Page<Episode> findAllByAnimeId(Long animeId, Pageable pageable);
}
