package dmo.server.exception;

public class AnimeStatusNotFoundException extends NotFoundException{
    public AnimeStatusNotFoundException(Long id) {
        super("AnimeStatus not found: " + id);
    }
}
