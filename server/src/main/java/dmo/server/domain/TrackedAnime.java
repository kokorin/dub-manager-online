package dmo.server.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Accessors(fluent = true)
@ToString(onlyExplicitlyIncluded = true)
public class TrackedAnime {
    @NonNull
    @ToString.Include
    private final Long animeId;

    @NonNull
    private final String userEmail;

    @NonNull
    @ToString.Include
    private final Status status;

    @NonNull
    private final Anime.Type type;

    @NonNull
    private final Set<AnimeTitle> titles;

    private Long episodeCount;
    private Long completedEpisodeCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextNotStartedEpisodeAirDate;
    private String comment;

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }
}
