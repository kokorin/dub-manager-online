package dmo.server.repository;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.domain.ExternalSystem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.annotation.Rollback;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

class AnimeRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    AnimeRepository repository;

    @Test
    @Rollback
    void findById() {
        var anime = repository.save(anime(1L));
        assertNotNull(anime.id());

        anime = repository.findById(anime.id()).orElseThrow();
        assertEquals(1L, anime.externalId());
        assertEquals(Anime.Type.SERIES, anime.type());
    }

    @Test
    @Rollback
    void delete() {
        var anime = repository.save(anime(1L));
        assertNotNull(anime.id());
        repository.delete(anime);
        assertTrue(repository.findById(anime.id()).isEmpty());

        anime = repository.save(anime(2L));
        assertNotNull(anime.id());
        repository.deleteById(anime.id());
        assertTrue(repository.findById(anime.id()).isEmpty());
    }

    @Test
    @Rollback
    void findByIdWithTitles() {
        var title1 = animeTitle(AnimeTitle.Type.OFFICIAL, "Title1");
        var title2 = animeTitle(AnimeTitle.Type.SHORT, "Title2");
        var anime = repository.save(anime(1L, title1, title2));
        assertNotNull(anime.id());
        assertThat(anime.titles(), containsInAnyOrder(title1, title2));

        anime = repository.findById(anime.id()).orElseThrow();
        assertEquals(1L, anime.externalId());
        assertEquals(Anime.Type.SERIES, anime.type());
        assertThat(anime.titles(), containsInAnyOrder(title1, title2));
    }

    @Test
    @Rollback
    void findAllByExternalSystem() {
        var anime1 = repository.save(anime(1L));
        anime1 = repository.findById(anime1.id()).orElseThrow();
        var anime2 = repository.save(anime(2L));
        anime2 = repository.findById(anime2.id()).orElseThrow();

        var animeList = repository.findAllByExternalSystem(ExternalSystem.ANIDB);
        assertEquals(2L, animeList.size());
        assertThat(animeList, containsInAnyOrder(anime1, anime2));
    }

    @Test
    @Rollback
    void externalIdAndSystemAreUnique() {
        var first = repository.save(anime(1L));
        assertNotNull(first.id());

        assertThrows(DbActionExecutionException.class, () -> repository.save(anime(1L)));
    }

    protected static Anime anime(Long externalId, AnimeTitle... titles) {
        return Anime.builder()
                .externalId(externalId)
                .externalSystem(ExternalSystem.ANIDB)
                .type(Anime.Type.SERIES)
                .titles(Set.of(titles))
                .build();
    }

    protected static AnimeTitle animeTitle(AnimeTitle.Type titleType, String title) {
        return new AnimeTitle(titleType, "eng", title);
    }
}