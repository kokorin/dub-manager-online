package dmo.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dub_user")
@Getter
@Setter
public class User {
    @Id
    private String email;
}
