package dmo.server.repository;

import com.p6spy.engine.spy.P6SpyDriver;
import dmo.server.AbstractDbTest;
import dmo.server.domain.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
@SpringJUnitConfig
@TestPropertySource(locations = "/application-test.properties")
@Transactional
public abstract class AbstractRepositoryTest extends AbstractDbTest {

    @DynamicPropertySource
    public static void updateConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", P6SpyDriver.class::getName);
        registry.add("spring.datasource.url",
                () -> database.getJdbcUrl().replaceFirst("jdbc:", "jdbc:p6spy:"));
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }
}
