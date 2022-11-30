package dmo.server;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

//TODO make JUnit5 extension
public abstract class AbstractDbTest {
    private static final DockerImageName MARIA_ALPINE = DockerImageName
            .parse("yobasystems/alpine-mariadb")
            .asCompatibleSubstituteFor("mariadb");

    public static final JdbcDatabaseContainer<?> database;

    static {
        database = new MariaDBContainer<>(MARIA_ALPINE)
                .withReuse(true)
                .withEnv("MYSQL_CHARSET", "utf8mb4")
                .withEnv("MYSQL_COLLATION", "utf8mb4_general_ci");
        database.start();
    }
}
