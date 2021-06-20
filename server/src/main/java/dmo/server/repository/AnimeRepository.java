package dmo.server.repository;

import dmo.server.domain.Anime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "titles")
    @NonNull
    Optional<Anime> findById(@NonNull Long id);

    @Query("FROM Anime a")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "titles")
    @NonNull
    List<Anime> findAllWithTitles();

    @Query("FROM Anime a WHERE a.id IN (:ids)")
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "titles")
    @NonNull
    List<Anime> findAllWithTitles(@NonNull @Param("ids") List<Long> ids, @NonNull Sort sort);

    @Query(value = "SELECT DISTINCT a.id FROM anime a WHERE a.id IN (" +
            "           SELECT t.anime_id FROM anime_title t" +
            "           WHERE MATCH(t.text) AGAINST(:title)" +
            "       )", nativeQuery = true)
    @NonNull
    List<Long> findIdByTitle(@NonNull @Param("title") String title, @NonNull Pageable pageable);

    @Query(value = "SELECT COUNT(DISTINCT t.anime_id) FROM anime_title t" +
            "       WHERE MATCH(t.text) AGAINST(:title)", nativeQuery = true)
    @NonNull
    Long countByTitle(@NonNull @Param("title") String title);

    @Query("FROM Anime WHERE id = (SELECT MAX(id) FROM Anime WHERE episodeCount IS NULL)")
    @NonNull
    Anime findAnimeWithoutEpisodes();
}