package dmo.server.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Getter
public class AnimeStatus {
    @EmbeddedId
    private AnimeStatusId id;

    @ManyToOne(optional = false)
    @MapsId("animeId")
    @Setter
    private Anime anime;

    @ManyToOne(optional = false)
    @MapsId("userEmail")
    @Setter
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Progress progress = Progress.NOT_STARTED;

    @Column(length = 4_096)
    @Setter
    private String comment;

    /**
     * Number of completed {@link Episode.Type#REGULAR regular} episodes.
     */
    @Column(nullable = false)
    @Setter
    private Long regularEpisodeCompleteCount;

    /**
     * Total number of {@link Episode.Type#REGULAR regular} episodes.
     */
    @Column(nullable = false)
    @Setter
    private Long regularEpisodeTotalCount;

    /**
     * Next {@link Episode.Type#REGULAR regular} {@link EpisodeStatus.Progress#NOT_STARTED not started} episode
     * air date.
     */
    @Column
    @Setter
    private LocalDate regularEpisodeNextAirDate;

    @PrePersist
    @PreUpdate
    private void updateId() {
        this.id = new AnimeStatusId(
                Optional.ofNullable(anime).map(Anime::getId).orElse(null),
                Optional.ofNullable(user).map(User::getEmail).orElse(null)
        );
    }

    public enum Progress {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnimeStatusId implements Serializable {
        private static final long serialVersionUID = 1;

        private Long animeId;
        private String userEmail;
    }
}
