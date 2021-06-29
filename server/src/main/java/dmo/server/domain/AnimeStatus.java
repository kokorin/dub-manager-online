package dmo.server.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Getter
@Setter
public class AnimeStatus {
    @EmbeddedId
    private AnimeStatusId id;

    @ManyToOne(optional = false)
    @MapsId("animeId")
    private Anime anime;

    @ManyToOne(optional = false)
    @MapsId("userId")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress = Progress.NOT_STARTED;

    @Column(length = 4_096)
    private String comment;

    /**
     * Number of completed regular episodes.
     */
    @Column(nullable = false)
    private Long completedRegularEpisodes;

    /**
     * Total number of regular episodes.
     */
    @Column(nullable = false)
    private Long totalRegularEpisodes;

    // Suppress Lombok setter generation
    private void setId(AnimeStatusId id) {
        this.id = id;
    }

    @PrePersist
    @PreUpdate
    private void updateId() {
        setId(new AnimeStatusId(
                Optional.ofNullable(anime).map(Anime::getId).orElse(null),
                Optional.ofNullable(user).map(User::getId).orElse(null)
        ));
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
        private Long userId;
    }
}
