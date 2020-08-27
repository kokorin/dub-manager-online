package dmo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dmo.server.domain.Anime;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    
}