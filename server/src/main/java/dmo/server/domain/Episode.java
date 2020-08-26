package dmo.server.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Episode {
    @Id
    private Long id;

    private Long number;

    private String title;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Anime anime;

    public enum Type {

    }
}