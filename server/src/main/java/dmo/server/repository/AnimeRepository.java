package dmo.server.repository;

import dmo.server.domain.Anime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    @Query("FROM Anime a")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "titles")
    List<Anime> findAllWithTitles();

    @Query("FROM Anime a WHERE a.id IN (:ids)")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "titles")
    List<Anime> findAllWithTitles(@Param("ids") List<Long> ids, Sort sort);

    @Query(value = "SELECT a.id FROM anime a WHERE a.id IN (" +
            "           SELECT t.anime_id FROM anime_title t" +
            "           WHERE MATCH(t.text) AGAINST(:title)" +
            "       )", nativeQuery = true)
    List<Long> findIdByTitle(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT COUNT(distinct t.anime_id) FROM anime_title t" +
            "       WHERE MATCH(t.text) AGAINST(:title)", nativeQuery = true)
    Long countByTitle(@Param("title") String title);
}