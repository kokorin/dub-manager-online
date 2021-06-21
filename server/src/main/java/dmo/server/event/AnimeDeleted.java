package dmo.server.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AnimeDeleted {
    private final Long animeId;
}
