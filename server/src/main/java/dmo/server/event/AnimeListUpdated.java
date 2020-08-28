package dmo.server.event;

import dmo.server.domain.Anime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AnimeListUpdated {
    private final List<Anime> animeList;
}
