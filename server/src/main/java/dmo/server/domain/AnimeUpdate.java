package dmo.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
public class AnimeUpdate {
    @Id
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id")
    @MapsId
    @Setter
    private Anime anime;

    @Column(nullable = false)
    @Setter
    private Instant lastUpdated;
}
