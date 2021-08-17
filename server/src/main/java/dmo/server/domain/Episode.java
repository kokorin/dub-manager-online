package dmo.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
public class Episode {
    @Id
    @Setter
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Type type;

    @Column(nullable = false)
    @Setter
    private Long number;

    @ElementCollection
    @CollectionTable(name = "episode_title")
    @Setter
    private List<EpisodeTitle> titles;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Setter
    private Anime anime;

    @Column(name = "anime_id", insertable = false, updatable = false)
    private Long animeId;

    @Setter
    private Long length;

    @Setter
    private LocalDate airDate;

    public enum Type {
        /**
         * Normal or Regular.
         */
        REGULAR,
        SPECIAL,
        /**
         * Opening/Ending.
         */
        CREDIT,
        /**
         * Trailer/Promo/Commercial.
         */
        TRAILER,
        PARODY,
        OTHER
    }
}