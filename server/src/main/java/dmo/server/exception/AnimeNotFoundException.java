package dmo.server.exception;

public class AnimeNotFoundException extends NotFoundException{
    public AnimeNotFoundException(Long id) {
        super("Anime not found: " + id);
    }
}
