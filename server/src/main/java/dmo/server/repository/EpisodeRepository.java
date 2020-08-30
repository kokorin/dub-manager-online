package dmo.server.repository;

import dmo.server.domain.Anime;
import dmo.server.domain.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    Page<Episode> findAllByAnime(Pageable pageable, Anime anime);
}