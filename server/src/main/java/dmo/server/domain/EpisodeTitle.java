package dmo.server.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class EpisodeTitle {

    @Column(nullable = false)
    private String lang;

    @Column(nullable = false)
    private String text;
}
