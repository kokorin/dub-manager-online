package dmo.server.repository;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.domain.TrackedAnime;
import dmo.server.exception.AnimeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class TrackedAnimeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<TrackedAnime> findById(Long animeId, String userEmail) {
        var sql = """
                select
                  anime.id as anime_id,
                  tracked.user_email,
                  tracked.status,
                  anime.type,
                  anime.episode_count,
                  tracked.completed_episode_count,
                  anime.start_date,
                  anime.end_date,
                  tracked.next_not_started_episode_air_date,
                  tracked.comment,
                  title.type as title_type,
                  title.lang as title_lang,
                  title.text as title_text
                from tracked_anime tracked
                join anime on tracked.anime_id = anime.id
                left join anime_title title on title.anime_id = anime.id
                where tracked.anime_id = :animeId
                and tracked.user_email = :userEmail
                order by anime.id
                """;

        var src = new MapSqlParameterSource("animeId", animeId)
                .addValue("userEmail", userEmail);
        var rse = new TrackedAnimeResultSetExtractor();
        return jdbcTemplate.query(sql, src, rse).stream().findFirst();
    }

   /* public Slice<TrackedAnime> findAll(String userEmail, Pageable pageable) {

    }*/

    public TrackedAnime updateStatus(Long animeId, String userEmail, TrackedAnime.Status status) {
        var sql = """
                insert into tracked_anime
                (anime_id, user_email, status)
                values
                (:animeId, :userEmail, :status)
                on duplicate key update status = :status
                """;

        var src = new MapSqlParameterSource("animeId", animeId)
                .addValue("userEmail", userEmail)
                .addValue("status", status.name());
        jdbcTemplate.update(sql, src);
        return findById(animeId, userEmail).orElseThrow(() -> new AnimeNotFoundException(animeId));
    }

    static record TrackedAnimeResultSetRow(
            Long animeId,
            String userEmail,
            TrackedAnime.Status status,
            Anime.Type type,
            Long episodeCount,
            Long completedEpisodeCount,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate nextNotStartedEpisodeAirDate,
            String comment,
            AnimeTitle.Type titleType,
            String titleLang,
            String titleText
    ) {
    }

    private static class TrackedAnimeResultSetExtractor implements ResultSetExtractor<List<TrackedAnime>> {

        @Override
        public List<TrackedAnime> extractData(@NotNull ResultSet rs) throws SQLException, DataAccessException {
            var mapper = new DataClassRowMapper<>(TrackedAnimeResultSetRow.class);
            List<TrackedAnime> result = new ArrayList<>();
            List<AnimeTitle> titles = new ArrayList<>();

            TrackedAnimeResultSetRow prevRow = null;
            while (rs.next()) {
                var row = mapper.mapRow(rs, 0);

                if (prevRow != null && !prevRow.animeId().equals(row.animeId())) {
                    var trackedAnime = toTrackedAnime(row, titles);
                    result.add(trackedAnime);
                    titles = new ArrayList<>();
                }

                var title = toAnimeTitle(row);
                titles.add(title);
                prevRow = row;
            }

            if (prevRow != null) {
                var trackedAnime = toTrackedAnime(prevRow, titles);
                result.add(trackedAnime);
            }

            return result;
        }

        private static AnimeTitle toAnimeTitle(TrackedAnimeResultSetRow row) {
            return new AnimeTitle(
                    row.titleType,
                    row.titleLang,
                    row.titleText
            );
        }

        private static TrackedAnime toTrackedAnime(TrackedAnimeResultSetRow row, List<AnimeTitle> titles) {
            return TrackedAnime.builder()
                    .animeId(row.animeId())
                    .userEmail(row.userEmail())
                    .status(row.status())
                    .type(row.type())
                    .episodeCount(row.episodeCount())
                    .completedEpisodeCount(row.completedEpisodeCount())
                    .startDate(row.startDate())
                    .endDate(row.endDate())
                    .nextNotStartedEpisodeAirDate(row.nextNotStartedEpisodeAirDate())
                    .comment(row.comment())
                    .titles(Set.copyOf(titles))
                    .build();
        }
    }

}
