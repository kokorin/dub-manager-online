package dmo.server.domain;

import lombok.NonNull;

public record AnimeTitle(
        @NonNull Type type,
        @NonNull String lang,
        @NonNull String text
) {
    public enum Type {
        SHORT,
        OFFICIAL
    }
}
