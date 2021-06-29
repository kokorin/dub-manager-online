package dmo.server.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Entity
public class EpisodeStatus {

    @EmbeddedId
    private EpisodeStatusId id;

    @ManyToOne(optional = false)
    @MapsId("episodeId")
    @Getter
    @Setter
    private Episode episode;

    @Column(name = "episode_id", insertable = false, updatable = false)
    private Long episodeId;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @Getter
    @Setter
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private Progress progress = Progress.NOT_STARTED;

    private void setId(EpisodeStatusId id) {
        this.id = id;
    }

    @PrePersist
    @PreUpdate
    private void updateId() {
        setId(new EpisodeStatusId(
                Optional.ofNullable(episode).map(Episode::getId).orElse(null),
                Optional.ofNullable(user).map(User::getId).orElse(null)
        ));
    }

    public enum Progress {
        NOT_STARTED,
        COMPLETED
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EpisodeStatusId implements Serializable {
        private static final long serialVersionUID = 1;

        private Long episodeId;
        private Long userId;
    }
}
