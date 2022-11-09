package dmo.server.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AnimeTitleTest {
    @Test
    void nonNull() {
        assertThrows(NullPointerException.class, () -> new AnimeTitle(null, "", ""));
        assertThrows(NullPointerException.class, () -> new AnimeTitle(AnimeTitle.Type.OFFICIAL, null, ""));
        assertThrows(NullPointerException.class, () -> new AnimeTitle(AnimeTitle.Type.OFFICIAL, "", null));
    }
}