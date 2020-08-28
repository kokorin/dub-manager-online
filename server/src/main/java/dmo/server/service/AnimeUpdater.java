package dmo.server.service;

import dmo.server.event.AnimeListUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnimeUpdater {
    private final AnimeService animeService;

    @EventListener
    public void onAnimeListUpdate(AnimeListUpdated animeListUpdated) {
        animeService.saveAll(animeListUpdated.getAnimeList());
    }
}
