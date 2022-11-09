package dmo.server.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EpisodeTitleTest {
    @Test
    void nonNull() {
        assertThrows(NullPointerException.class, () -> new EpisodeTitle(null, ""));
        assertThrows(NullPointerException.class, () -> new EpisodeTitle("", null));
    }
}