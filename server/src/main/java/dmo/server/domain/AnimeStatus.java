package dmo.server.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
    private Status status;

    // Suppress Lombok setter generation
    private void setId(AnimeStatusId id) {
        this.id = id;
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
