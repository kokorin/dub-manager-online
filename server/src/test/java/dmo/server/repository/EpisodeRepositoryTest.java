package dmo.server.repository;

import dmo.server.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

class EpisodeRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    AnimeRepository animeRepository;

    @Autowired
    EpisodeRepository repository;

    private Long animeId;

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

        animeId = animeRepository.save(anime).id();
    }

    @Test
    @Rollback
    void findAllByAnimeId() {
        var expected = repository.saveAll(Arrays.asList(
                episode(animeId, 1L, 1L),
                episode(animeId, 2L, 2L),
                episode(animeId, 3L, 3L)
        ));

        var actual = repository.findAllByAnimeId(animeId, Pageable.ofSize(10)).getContent();

        assertThat(expected, containsInAnyOrder(actual.toArray()));
    }

    protected static Episode episode(Long animeId, Long externalId, Long number, EpisodeTitle... titles) {
        return Episode.builder()
                .animeId(animeId)
                .externalId(externalId)
                .number(number)
                .titles(Set.of(titles))
                .build();
    }
}