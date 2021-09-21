package dmo.server.exception;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeStatus;
import dmo.server.domain.User;

public class AnimeStatusNotFoundException extends NotFoundException {
    public AnimeStatusNotFoundException(User user, Anime anime) {
        this(new AnimeStatus.AnimeStatusId(anime.getId(), user.getEmail()));
    }

    public AnimeStatusNotFoundException(AnimeStatus.AnimeStatusId id) {
        super("AnimeStatus not found: " + id);
    }
}
