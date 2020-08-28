package dmo.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Anime {
    @Id
    private Long id;

    @ElementCollection
    @CollectionTable(name = "anime_title")
    private List<AnimeTitle> titles;

    @Enumerated(EnumType.STRING)
    private Type type;

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