package dmo.server.exception;

import dmo.server.domain.EpisodeStatus;

public class EpisodeStatusNotFoundException extends NotFoundException{
    public EpisodeStatusNotFoundException(EpisodeStatus.EpisodeStatusId id) {
        super("EpisodeStatus not found: " + id);
    }
}
