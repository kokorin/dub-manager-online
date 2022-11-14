package dmo.server.repository;

import dmo.server.domain.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

class TrackedEpisodeRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    AnimeRepository animeRepository;
    @Autowired
    EpisodeRepository episodeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TrackedEpisodeRepository repository;

    Long episodeId;
    String userEmail;

    @BeforeEach
    public void createAnime() {
        // Executed inside transaction, no need to delete anime after
        var anime = Anime.builder()
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Set.of(
                        new AnimeTitle(AnimeTitle.Type.OFFICIAL, "eng", "title")
                ))
                .type(Anime.Type.SERIES)
                .episodeCount(4L)
                .startDate(LocalDate.of(1986, Month.SEPTEMBER, 24))
                .endDate(LocalDate.of(1986, Month.DECEMBER, 24))
                .deleted(false)
                .build();

        var animeId = animeRepository.save(anime).id();

        var episode = Episode.builder()
                .animeId(animeId)
                .externalId(43L)
                .number(1L)
                .titles(Set.of(
                        new EpisodeTitle("eng", "title-1")
                ))
                .airDate(LocalDate.of(2013, Month.NOVEMBER, 13))
                .build();
        episodeId = episodeRepository.save(episode).id();

        userEmail = userRepository.save(new User("name@site.domain")).email();
    }

    @Test
    @Rollback
    void findById() {
        assertTrue(repository.findById(episodeId, userEmail).isEmpty());
    }

    @Test
    @Rollback
    void updateStatus() {
        assertTrue(repository.findById(episodeId, userEmail).isEmpty());

        var tracked = repository.updateStatus(episodeId, userEmail, true);
        assertTrue(tracked.completed());
        assertEquals(1L, tracked.number());
        assertEquals(LocalDate.of(2013, Month.NOVEMBER, 13), tracked.airDate());
        assertThat(tracked.titles(), containsInAnyOrder(new EpisodeTitle("eng", "title-1")));

        tracked = repository.updateStatus(episodeId, userEmail, false);
        assertFalse(tracked.completed());
        assertEquals(1L, tracked.number());
    }
}