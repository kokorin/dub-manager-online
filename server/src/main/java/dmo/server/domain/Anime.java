package dmo.server.domain;

import javax.persistence.Entity;
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
}