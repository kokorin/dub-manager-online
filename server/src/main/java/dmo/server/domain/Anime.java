package dmo.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(of = {"id", "type"})
public class Anime {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    public Long episodeCount;

    public LocalDate startDate;

    public LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "anime_title")
    private Set<AnimeTitle> titles;

    public enum Type {
        MOVIE,
        OVA,
        TV_SERIES,
        TV_SPECIAL,
        WEB,
        MUSIC_VIDEO,
        OTHER,
        UNKNOWN;
    }

}