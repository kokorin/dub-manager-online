package dmo.server.exception;

public class TrackedAnimeNotFoundException extends NotFoundException {

    public TrackedAnimeNotFoundException(String userEmail, Long animeId ) {
        super("AnimeStatus not found: " + animeId + " for user " + userEmail);
    }
}
