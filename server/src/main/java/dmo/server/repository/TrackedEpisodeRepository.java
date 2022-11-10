package dmo.server.repository;

import dmo.server.domain.EpisodeTitle;
import dmo.server.domain.TrackedEpisode;
import dmo.server.exception.TrackedEpisodeNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class TrackedEpisodeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<TrackedEpisode> findById(Long episodeId, String userEmail) {
        var sql = """
                select
                  tracked.episode_id,
                  tracked.user_email,
                  episode.anime_id,
                  episode.number,
                  tracked.completed,
                  episode.length,
                  episode.air_date,
                  title.lang as title_lang,
                  title.text as title_text
                from tracked_episode tracked
                join episode on episode.id = tracked.episode_id
                left join episode_title title on title.episode_id = episode.id
                where episode.id = :episodeId
                and tracked.user_email = :userEmail
                order by episode.id
                """;
        var src = new MapSqlParameterSource("episodeId", episodeId)
                .addValue("userEmail", userEmail);
        var rse = new TrackedEpisodeResultSetExtractor();
        return jdbcTemplate.query(sql, src, rse).stream().findFirst();
    }

    public TrackedEpisode updateStatus(Long episodeId, String userEmail, Boolean completed) {
        var sql = """
                insert into tracked_episode
                (episode_id, user_email, completed)
                values
                (:episodeId, :userEmail, :completed)
                on duplicate key update completed = :completed
                """;

        var src = new MapSqlParameterSource("episodeId", episodeId)
                .addValue("userEmail", userEmail)
                .addValue("completed", completed);
        jdbcTemplate.update(sql, src);
        return findById(episodeId, userEmail).orElseThrow(() -> new TrackedEpisodeNotFoundException(userEmail, episodeId));

    }

    static record TrackedEpisodeResultSetRow(
            Long episodeId,
            String userEmail,
            Long animeId,
            Long number,
            Boolean completed,
            Long length,
            LocalDate airDate,
            String titleLang,
            String titleText
    ) {
    }

    private static class TrackedEpisodeResultSetExtractor implements ResultSetExtractor<List<TrackedEpisode>> {
        @Override
        public List<TrackedEpisode> extractData(@NotNull ResultSet rs) throws SQLException, DataAccessException {
            var mapper = new DataClassRowMapper<>(TrackedEpisodeResultSetRow.class);
            List<TrackedEpisode> result = new ArrayList<>();
            List<EpisodeTitle> titles = new ArrayList<>();

            TrackedEpisodeResultSetRow prevRow = null;
            while (rs.next()) {
                var row = mapper.mapRow(rs, 0);
                if (row == null) {
                    continue;
                }

                if (prevRow != null && !prevRow.animeId().equals(row.animeId())) {
                    var trackedEpisode = toTrackedEpisode(row, titles);
                    result.add(trackedEpisode);
                    titles = new ArrayList<>();
                }

                var title = toEpisodeTitle(row);
                titles.add(title);
                prevRow = row;
            }

            if (prevRow != null) {
                var trackedEpisode = toTrackedEpisode(prevRow, titles);
                result.add(trackedEpisode);
            }

            return result;
        }

        private EpisodeTitle toEpisodeTitle(TrackedEpisodeResultSetRow row) {
            return new EpisodeTitle(row.titleLang, row.titleText);
        }

        private TrackedEpisode toTrackedEpisode(TrackedEpisodeResultSetRow row, List<EpisodeTitle> titles) {
            return TrackedEpisode.builder()
                    .animeId(row.animeId)
                    .userEmail(row.userEmail)
                    .episodeId(row.episodeId)
                    .number(row.number)
                    .completed(row.completed)
                    .length(row.length)
                    .airDate(row.airDate)
                    .titles(Set.copyOf(titles))
                    .build();
        }
    }
}
