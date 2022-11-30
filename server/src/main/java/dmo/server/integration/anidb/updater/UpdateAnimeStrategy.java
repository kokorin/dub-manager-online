package dmo.server.integration.anidb.updater;

import dmo.server.domain.Anime;
import dmo.server.domain.ExternalSystem;
import dmo.server.integration.anidb.dto.AnidbAnimeTitles;
import dmo.server.integration.anidb.mapper.AnidbAnimeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateAnimeStrategy {
    private final AnidbAnimeMapper mapper;

    /**
     * Finds list of changed or deleted Anime and builds a list which contains only changed Anime.
     *
     * @param knownAnimeList       lsit of known Anime (stored in DB)
     * @param freshAnimeTitleList list of fresh Anime Titles
     * @return list containing only changed Anime
     */
    public List<Anime> changedAnime(List<Anime> knownAnimeList, List<AnidbAnimeTitles> freshAnimeTitleList) {
        long updateCount = 0;
        long newCount = 0;

        List<Anime> result = new ArrayList<>();
        var persistedAnimeByExternalId = knownAnimeList.stream()
                .collect(toMap(Anime::externalId, identity()));

        // For each Anime retrieved from AniDB check if we need to update it
        for (var animeTitles : freshAnimeTitleList) {
            var titles = mapper.toAnimeTitles(animeTitles.titles);
            Anime persisted = persistedAnimeByExternalId.get(animeTitles.id);
            // Anime with the same externalId is already in DB
            if (persisted != null) {
                var sameTitles = Objects.equals(persisted.titles(), titles);
                // if anime appears in titles list - it is not deleted
                if (!sameTitles || persisted.deleted()) {
                    result.add(
                            persisted
                                    .deleted(false)
                                    .titles(titles)
                    );
                    updateCount++;
                }
            } else {
                // Anime not in DB
                result.add(
                        Anime.builder()
                                .externalId(animeTitles.id)
                                .externalSystem(ExternalSystem.ANIDB)
                                .titles(titles)
                                .deleted(false)
                                .type(Anime.Type.UNKNOWN)
                                .build()
                );
                newCount++;
            }
        }


        var animeExternalIds = freshAnimeTitleList.stream()
                .map(at -> at.id)
                .collect(toSet());
        var deleteCount = persistedAnimeByExternalId.keySet().stream()
                .filter(id -> !animeExternalIds.contains(id))
                .map(deletedId ->
                        result.add(
                                persistedAnimeByExternalId.get(deletedId)
                                        .deleted(true)
                        )
                )
                .count();

        log.info("Got: {}, new: {}, updated: {}, deleted: {}", freshAnimeTitleList.size(), newCount, updateCount, deleteCount);

        return result;
    }
}
