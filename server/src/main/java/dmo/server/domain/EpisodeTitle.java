package dmo.server.domain;

import lombok.NonNull;

public record EpisodeTitle(
        @NonNull String lang,
        @NonNull String text
) {
}
