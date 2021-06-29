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

@Repository
public interface EpisodeStatusRepository extends JpaRepository<EpisodeStatus, EpisodeStatus.EpisodeStatusId> {
    @Query("SELECT es\n" +
            "FROM EpisodeStatus es\n" +
            "JOIN Episode e ON es.id.episodeId = e.id\n" +
            "WHERE e.anime = :anime\n" +
            "AND es.user = :user")
    Page<EpisodeStatus> findAllByAnimeAndUser(Anime anime, User user, Pageable pageable);

    default Integer fillEpisodeStatuses(Anime anime) {
        return fillEpisodeStatuses(anime, null);
    }

    @Modifying
    @Query("INSERT INTO EpisodeStatus(episodeId, userId)\n" +
            "SELECT e.id, u.id\n" +
            "FROM AnimeStatus st\n" +
            "JOIN User u ON u.id = st.id.userId\n" +
            "JOIN Episode e ON e.anime.id = st.id.animeId\n" +
            "JOIN Anime a ON a.id = st.id.animeId\n" +
            "WHERE a = :anime\n" +
            "AND (u = :user OR :user IS NULL)\n" +
            "AND (e.id, u.id) NOT IN (SELECT es.episode.id, es.user.id FROM EpisodeStatus es)")
    Integer fillEpisodeStatuses(Anime anime, User user);

}
