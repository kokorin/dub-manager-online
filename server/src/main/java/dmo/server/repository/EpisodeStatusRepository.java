package dmo.server.repository;

import dmo.server.domain.Anime;
import dmo.server.domain.EpisodeStatus;
import dmo.server.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EpisodeStatusRepository extends JpaRepository<EpisodeStatus, EpisodeStatus.EpisodeStatusId> {
    @Query("SELECT es " +
            "FROM EpisodeStatus es " +
            "JOIN Episode e ON es.id.episodeId = e.id " +
            "WHERE e.anime = :anime " +
            "AND es.user = :user")
    Page<EpisodeStatus> findAllByAnimeAndUser(Anime anime, User user, Pageable pageable);

    @Query("SELECT MIN(es.episode.airDate) " +
            "FROM EpisodeStatus es " +
            "WHERE es.episode.anime = :anime " +
            "AND es.user = :user " +
            "AND es.episode.type = 'REGULAR' " +
            "AND es.progress = 'NOT_STARTED'")
    LocalDate getRegularEpisodeNextAirDate(Anime anime, User user);

    @Query("SELECT COUNT(es.episode.id) " +
            "FROM EpisodeStatus es " +
            "WHERE es.episode.anime = :anime " +
            "AND es.user = :user " +
            "AND es.episode.type = 'REGULAR' " +
            "AND es.progress =  'COMPLETED'")
    Long getRegularEpisodeCompleteCount(Anime anime, User user);

    @Modifying
    @Query("INSERT INTO EpisodeStatus(episodeId, userEmail) " +
            "SELECT e.id, u.email " +
            "FROM AnimeStatus ast " +
            "JOIN User u ON u.email = ast.id.userEmail " +
            "JOIN Episode e ON e.anime.id = ast.id.animeId " +
            "JOIN Anime a ON a.id = ast.id.animeId " +
            "WHERE a = :anime " +
            "AND (e.id, u.email) NOT IN (SELECT es.episode.id, es.user.email FROM EpisodeStatus es)")
    Integer fillEpisodeStatusesForAllUsers(Anime anime);

    @Modifying
    @Query("INSERT INTO EpisodeStatus(episodeId, userEmail) " +
            "SELECT e.id, u.email " +
            "FROM Episode e, User u " +
            "WHERE e.anime = :anime " +
            "AND u = :user " +
            "AND (e.id, u.email) NOT IN (SELECT es.episode.id, es.user.email FROM EpisodeStatus es)")
    Integer fillEpisodeStatusesForUser(Anime anime, User user);

    @Modifying
    @Query("DELETE FROM EpisodeStatus " +
            "WHERE episode IN (SELECT e FROM Episode e WHERE e.anime.id = :animeId) " +
            "AND user.email = :userEmail")
    void deleteAllByAnimeAndUser(Long animeId, String userEmail);
}
