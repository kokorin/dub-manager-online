package dmo.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dub_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String email;
}
