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
    private Status status;

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

    public enum Status {
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
