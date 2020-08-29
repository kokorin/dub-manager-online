package dmo.server.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Data
public class AnimeTitle {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String lang;

    @Column(nullable = false)
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
