package dmo.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Episode {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private Long number;

    @ElementCollection
    @CollectionTable(name = "episode_title")
    private List<EpisodeTitle> titles;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Anime anime;

    private Long length;

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