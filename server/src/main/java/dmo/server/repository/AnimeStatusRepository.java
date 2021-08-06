package dmo.server.repository;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeStatus;
import dmo.server.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimeStatusRepository extends JpaRepository<AnimeStatus, AnimeStatus.AnimeStatusId> {
    Page<AnimeStatus> findAllByUser(User user, Pageable pageable);

    Optional<AnimeStatus> findByUserAndAnime(User user, Anime anime);

    @Query("UPDATE AnimeStatus s" +
            " SET s.totalRegularEpisodes = (SELECT a.episodeCount FROM Anime a WHERE a = :anime)" +
            " WHERE s.anime = :anime")
    @Modifying
    int updateTotalRegularEpisodes(Anime anime);

    @Query("UPDATE AnimeStatus a\n" +
            "SET a.completedRegularEpisodes = (\n" +
            "  SELECT COUNT(1)\n" +
            "  FROM EpisodeStatus e\n" +
            "  WHERE e.episode.anime = :anime\n" +
            "  AND e.user = :user" +
            "  AND e.progress = 'COMPLETED'" +
            ")\n" +
            "WHERE a.anime = :anime\n" +
            "AND a.user = :user")
    @Modifying
    int updateCompletedRegularEpisodes(Anime anime, User user);
}
