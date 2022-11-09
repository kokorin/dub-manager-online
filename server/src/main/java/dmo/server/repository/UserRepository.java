package dmo.server.repository;

import dmo.server.domain.User;
import dmo.server.exception.AnimeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository  {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public User save(User user) {
        var sql = """
                insert ignore into dmo_user
                (email)
                values
                (:email)
                """;

        var src = new BeanPropertySqlParameterSource(user);
        jdbcTemplate.update(sql, src);
        return user;
    }
}
