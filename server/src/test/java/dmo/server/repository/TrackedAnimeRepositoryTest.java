package dmo.server.repository;

import dmo.server.domain.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TrackedAnimeRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    AnimeRepository animeRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TrackedAnimeRepository repository;

    private Long animeId;
    private String userEmail;

    @BeforeEach
    public void createAnime() {
        // Executed inside transaction, no need to delete anime after
        var anime= Anime.builder()
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Set.of(
                        new AnimeTitle(AnimeTitle.Type.OFFICIAL, "eng", "title")
                ))
                .type(Anime.Type.SERIES)
                .episodeCount(4L)
                .startDate(LocalDate.of(1986, Month.SEPTEMBER, 24))
                .endDate(LocalDate.of(1986, Month.DECEMBER, 24))
                .build();
        animeId = animeRepository.save(anime).id();
        userEmail = userRepository.save(new User("name@site.domain")).email();
    }

    @Test
    @Rollback
    void findByIdReturnsNoneWhenNoAnimeIsTracked() {
        assertFalse(repository.findById(animeId, userEmail).isPresent());
    }

    @Test
    @Rollback
    void updateStatus() {
        var count = jdbcTemplate.queryForObject("select count(1) from tracked_anime", Long.class);
        assertEquals(0L, count);

        var trackedAnime = repository.updateStatus(animeId, userEmail, TrackedAnime.Status.NOT_STARTED);
        count = jdbcTemplate.queryForObject("select count(1) from tracked_anime", Long.class);
        assertEquals(1L, count);
        assertEquals(trackedAnime.animeId(), animeId);
        assertEquals(trackedAnime.userEmail(), userEmail);
        assertEquals(trackedAnime.status(), TrackedAnime.Status.NOT_STARTED);
        assertEquals(trackedAnime.type(), Anime.Type.SERIES);
        assertEquals(trackedAnime.episodeCount(), 4L);
        assertEquals(trackedAnime.startDate(), LocalDate.of(1986, Month.SEPTEMBER, 24));
        assertEquals(trackedAnime.endDate(), LocalDate.of(1986, Month.DECEMBER, 24));
        assertThat(trackedAnime.titles(), containsInAnyOrder(new AnimeTitle(AnimeTitle.Type.OFFICIAL, "eng", "title")));

        repository.updateStatus(animeId, userEmail, TrackedAnime.Status.NOT_STARTED);
        count = jdbcTemplate.queryForObject("select count(1) from tracked_anime", Long.class);
        assertEquals(1L, count);
    }
}