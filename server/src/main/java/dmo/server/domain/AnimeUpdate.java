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
    private Long animeId;

    @OneToOne(optional = false)
    @MapsId
    private Anime anime;

    @Column(nullable = false)
    private Instant lastUpdated;

    // Suppress Lombok setter generation
    private void setId(Long animeId) {
        this.animeId = animeId;
    }
}
