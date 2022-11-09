package dmo.server.domain;

import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import javax.validation.constraints.NotNull;

@Data
public class TrackedEpisode {

    @Id
    @NonNull
    private final ID id;

    @NonNull
    private Boolean completed;

    public AggregateReference<Episode, Long> getEpisode() {
        return AggregateReference.to(id.episodeId);
    }

    public AggregateReference<User, String> getUser() {
        return AggregateReference.to(id.userEmail);
    }

    @Value
    public static class ID {
        @NotNull Long episodeId;
        @NotNull String userEmail;
    }
}
