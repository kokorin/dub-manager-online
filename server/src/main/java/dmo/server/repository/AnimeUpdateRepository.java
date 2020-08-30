package dmo.server.repository;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimeUpdateRepository extends JpaRepository<AnimeUpdate, Long> {

    Optional<AnimeUpdate> findByAnime(Anime anime);
}
