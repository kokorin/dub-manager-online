package dmo.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Setter
@ToString
public class AnimeTitle {
    @Enumerated(EnumType.STRING)
    private Type type;
    private String lang;
    private String text;

    public enum Type {
        SHORT,
        OFFICIAL,
        SYNONYM,
        MAIN,
        CARD,
        KANA
        ;
    }
}
