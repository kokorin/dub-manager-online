package dmo.server.domain;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class Episode {
    @Id
    @ToString.Include
    private Long id;

    @NonNull
    @ToString.Include
    private final Long animeId;

    @NonNull
    @ToString.Include
    private final Long externalId;

    @ToString.Include
    private final Long number;

    @NonNull
    private Set<EpisodeTitle> titles = Collections.emptySet();

    private Long length;

    private LocalDate airDate;
}