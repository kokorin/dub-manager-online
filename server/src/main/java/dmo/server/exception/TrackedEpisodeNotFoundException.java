package dmo.server.exception;

public class TrackedEpisodeNotFoundException extends NotFoundException {
    public TrackedEpisodeNotFoundException(String userEmail, Long episodeId) {
        super("EpisodeStatus not found: " + episodeId + " for user: " + userEmail);
    }
}
