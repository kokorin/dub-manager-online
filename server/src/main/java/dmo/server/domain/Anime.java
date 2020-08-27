package dmo.server.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Anime {
    @Id
    private Long id;

    private String title;

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