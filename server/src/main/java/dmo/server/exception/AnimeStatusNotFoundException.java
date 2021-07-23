package dmo.server.exception;

import dmo.server.domain.AnimeStatus;

public class AnimeStatusNotFoundException extends NotFoundException{
    public AnimeStatusNotFoundException(AnimeStatus.AnimeStatusId id) {
        super("AnimeStatus not found: " + id);
    }
}
