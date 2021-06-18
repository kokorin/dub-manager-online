package dmo.server.service;

import dmo.server.domain.AnimeStatus;
import dmo.server.domain.User;
import dmo.server.exception.AnimeNotFoundException;
import dmo.server.exception.UserNotFoundException;
import dmo.server.repository.AnimeRepository;
import dmo.server.repository.AnimeStatusRepository;
import dmo.server.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class AnimeStatusService {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final AnimeRepository animeRepository;
    @NonNull
    private final AnimeStatusRepository animeStatusRepository;

    @Secured("ROLE_USER")
    @Transactional
    public Page<AnimeStatus> findAll(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return animeStatusRepository.findAllByUser(user, pageable);
    }

    @Secured("ROLE_USER")
    @Transactional
    public AnimeStatus updateAnimeStatus(Long userId, Long animeId, Consumer<AnimeStatus> updater) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        var anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException(animeId));

        var animeStatus = animeStatusRepository.findByUserAndAnime(user, anime)
                .orElseGet(() -> {
                    var result = new AnimeStatus();
                    result.setUser(user);
                    result.setAnime(anime);
                    return result;
                });

        updater.accept(animeStatus);
        // If AnimeStatus was created we have to save it
        animeStatusRepository.save(animeStatus);

        return animeStatus;
    }
}
