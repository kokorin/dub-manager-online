package dmo.server.repository;

import dmo.server.domain.Anime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    @Query("FROM Anime a JOIN a.titles t")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "titles")
    List<Anime> findAllWithTitles();
}