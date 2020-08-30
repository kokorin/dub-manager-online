package dmo.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
public class AnimeUpdate {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id")
    @MapsId
    private Anime anime;

    @Column(nullable = false)
    private Instant lastUpdated;
}
