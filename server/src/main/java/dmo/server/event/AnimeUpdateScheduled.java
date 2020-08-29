package dmo.server.event;

import dmo.server.domain.Anime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AnimeUpdateScheduled {
    private final Anime anime;
}
