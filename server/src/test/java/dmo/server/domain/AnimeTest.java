package dmo.server.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AnimeTest {
    @Test
    void nonNull() {
        var anime = Anime.builder()
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .type(Anime.Type.MOVIE)
                .titles(Collections.emptySet())
                .deleted(false)
                .build();
        assertThrows(NullPointerException.class, () -> Anime.builder()
                .externalSystem(ExternalSystem.ANIDB)
                .type(Anime.Type.MOVIE)
                .titles(Collections.emptySet())
                .deleted(false)
                .build()
        );
        assertThrows(NullPointerException.class, () -> Anime.builder()
                .externalId(42L)
                .type(Anime.Type.MOVIE)
                .titles(Collections.emptySet())
                .deleted(false)
                .build()
        );
        assertThrows(NullPointerException.class, () -> Anime.builder()
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Collections.emptySet())
                .deleted(false)
                .build()
        );
        assertThrows(NullPointerException.class, () -> Anime.builder()
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .type(Anime.Type.MOVIE)
                .deleted(false)
                .build()
        );
        assertThrows(NullPointerException.class, () -> Anime.builder()
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .type(Anime.Type.MOVIE)
                .titles(Collections.emptySet())
                .build()
        );
    }
}