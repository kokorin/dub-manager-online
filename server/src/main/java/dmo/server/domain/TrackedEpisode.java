package dmo.server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Accessors(fluent = true)
@ToString(onlyExplicitlyIncluded = true)
public class TrackedEpisode {

    @NonNull
    @ToString.Include
    private final Long episodeId;

    @NonNull
    private final String userEmail;

    @NonNull
    @ToString.Include
    private final Long animeId;

    @NonNull
    @ToString.Include
    private final Long number;

    @NonNull
    @ToString.Include
    private final Boolean completed;

    @NonNull
    private Set<EpisodeTitle> titles;

    private Long length;

    private LocalDate airDate;
}
