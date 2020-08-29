package dmo.server.event;

import dmo.server.domain.Anime;
import dmo.server.domain.Episode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class AnimeUpdated {
    private final Anime anime;
    private final List<Episode> episodes;
}
