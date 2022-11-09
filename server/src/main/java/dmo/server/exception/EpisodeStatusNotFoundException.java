package dmo.server.exception;

import dmo.server.domain.TrackedEpisode;

public class EpisodeStatusNotFoundException extends NotFoundException {
    public EpisodeStatusNotFoundException(TrackedEpisode.ID id) {
        super("EpisodeStatus not found: " + id);
    }
}
