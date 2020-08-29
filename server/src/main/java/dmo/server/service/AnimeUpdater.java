package dmo.server.service;

import dmo.server.domain.Anime;
import dmo.server.event.AnimeListUpdated;
import dmo.server.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnimeUpdater {
    private final AnimeRepository animeRepository;

    @EventListener
    @Transactional
    public void onAnimeListUpdate(AnimeListUpdated animeListUpdated) {
        //animeService.saveAll(animeListUpdated.getAnimeList());
        List<Anime> animeList = animeListUpdated.getAnimeList();
        List<Anime> persistedList = animeRepository.findAllWithTitles();

        Map<Long, Anime> persistedById = persistedList.stream()
                .collect(Collectors.toMap(Anime::getId, Function.identity()));

        long updated = 0;
        long inserted = 0;

        for (Anime anime : animeList) {
            Anime persisted = persistedById.get(anime.getId());
            if (persisted != null) {
                if (!Objects.equals(persisted.getTitles(), anime.getTitles())) {
                    persisted.setTitles(anime.getTitles());
                    updated++;
                }
            } else {
                anime.setType(Anime.Type.UNKNOWN);
                animeRepository.save(anime);
                inserted++;
            }
        }

        log.info("Got: {}, inserted: {}, updated: {}", animeList.size(), inserted, updated);
    }
}
