package dmo.server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Accessors(fluent = true)
@ToString(onlyExplicitlyIncluded = true)
public class Anime {
    @Id
    @ToString.Include
    private Long id;

    @NonNull
    @ToString.Include
    private final Long externalId;

    @NonNull
    @ToString.Include
    private final ExternalSystem externalSystem;

    @NonNull
    @ToString.Include
    private final Type type;

    private Long episodeCount;

    private LocalDate startDate;

    private LocalDate endDate;

    // TODO add @NotEmpty
    @NonNull
    private Set<AnimeTitle> titles;

    private Instant last_update;

    public enum Type {
        MOVIE,
        SERIES,
        MUSIC,
        UNKNOWN,
        DELETED;
    }

}