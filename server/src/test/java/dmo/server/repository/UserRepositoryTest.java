package dmo.server.repository;

import dmo.server.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    UserRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @Rollback
    void save() {
        var count = jdbcTemplate.queryForObject("select count(1) from dmo_user", Long.class);
        assertEquals(0L, count);

        repository.save(new User("username@email.com"));

        count = jdbcTemplate.queryForObject("select count(1) from dmo_user", Long.class);
        assertEquals(1L, count);
    }
}