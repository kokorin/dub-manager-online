package dmo.server.integration.anidb.updater;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.domain.ExternalSystem;
import dmo.server.integration.anidb.dto.AnidbAnimeTitle;
import dmo.server.integration.anidb.dto.AnidbAnimeTitles;
import dmo.server.integration.anidb.mapper.AnidbAnimeMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

class UpdateAnimeStrategyTest {
    UpdateAnimeStrategy strategy = new UpdateAnimeStrategy(Mappers.getMapper(AnidbAnimeMapper.class));

    @Test
    void findChangedAnimeReturnsEmptyListWhenNoChanges() {
        assertThat(strategy.changedAnime(emptyList(), emptyList()), empty());

        var animeTitles = new AnidbAnimeTitles(
                42L,
                Set.of(
                        new AnidbAnimeTitle(AnidbAnimeTitle.Type.OFFICIAL, "eng", "official"),
                        new AnidbAnimeTitle(AnidbAnimeTitle.Type.SHORT, "eng", "short")
                )
        );

        var anime = Anime.builder()
                .id(11L)
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Set.of(
                        new AnimeTitle(AnimeTitle.Type.OFFICIAL, "eng", "official"),
                        new AnimeTitle(AnimeTitle.Type.SHORT, "eng", "short")
                ))
                .type(Anime.Type.SERIES)
                .episodeCount(4L)
                .startDate(LocalDate.of(1986, Month.SEPTEMBER, 24))
                .endDate(LocalDate.of(1986, Month.DECEMBER, 24))
                .deleted(false)
                .build();

        assertThat(strategy.changedAnime(List.of(anime), List.of(animeTitles)), empty());
    }

    @Test
    void findChangedAnimeReturnsListWithNewAnime() {
        var newTitles = new AnidbAnimeTitles(
                101L,
                Set.of(
                        new AnidbAnimeTitle(AnidbAnimeTitle.Type.OFFICIAL, "eng", "off101"),
                        new AnidbAnimeTitle(AnidbAnimeTitle.Type.SHORT, "eng", "101")
                )
        );

        var newAnime = Anime.builder()
                .externalId(101L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Set.of(
                        new AnimeTitle(AnimeTitle.Type.OFFICIAL, "eng", "off101"),
                        new AnimeTitle(AnimeTitle.Type.SHORT, "eng", "101")
                ))
                .type(Anime.Type.UNKNOWN)
                .deleted(false)
                .build();

        assertThat(strategy.changedAnime(emptyList(), List.of(newTitles)), contains(newAnime));
    }

    @Test
    void findChangedAnimeReturnsUpdatedAnime() {
        var updatedTitles = new AnidbAnimeTitles(
                42L,
                Set.of(
                        new AnidbAnimeTitle(AnidbAnimeTitle.Type.OFFICIAL, "jpn", "ofisharu"),
                        new AnidbAnimeTitle(AnidbAnimeTitle.Type.SHORT, "eng", "short")
                )
        );

        var anime = Anime.builder()
                .id(1L)
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Set.of(
                        new AnimeTitle(AnimeTitle.Type.OFFICIAL, "eng", "official"),
                        new AnimeTitle(AnimeTitle.Type.SHORT, "eng", "short")
                ))
                .type(Anime.Type.SERIES)
                .episodeCount(4L)
                .startDate(LocalDate.of(1986, Month.SEPTEMBER, 24))
                .endDate(LocalDate.of(1986, Month.DECEMBER, 24))
                .deleted(false)
                .build();

        var updated = Anime.builder()
                .id(1L)
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Set.of(
                        new AnimeTitle(AnimeTitle.Type.OFFICIAL, "jpn", "ofisharu"),
                        new AnimeTitle(AnimeTitle.Type.SHORT, "eng", "short")
                ))
                .type(Anime.Type.SERIES)
                .episodeCount(4L)
                .startDate(LocalDate.of(1986, Month.SEPTEMBER, 24))
                .endDate(LocalDate.of(1986, Month.DECEMBER, 24))
                .deleted(false)
                .build();

        assertThat(strategy.changedAnime(List.of(anime), List.of(updatedTitles)), contains(updated));
    }

    @Test
    void findChangedAnimeReturnsDeletedAnime() {
        var anime = Anime.builder()
                .id(1L)
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Set.of(
                        new AnimeTitle(AnimeTitle.Type.OFFICIAL, "eng", "official"),
                        new AnimeTitle(AnimeTitle.Type.SHORT, "eng", "short")
                ))
                .type(Anime.Type.SERIES)
                .episodeCount(4L)
                .startDate(LocalDate.of(1986, Month.SEPTEMBER, 24))
                .endDate(LocalDate.of(1986, Month.DECEMBER, 24))
                .deleted(false)
                .build();

        var deleted = Anime.builder()
                .id(1L)
                .externalId(42L)
                .externalSystem(ExternalSystem.ANIDB)
                .titles(Set.of(
                        new AnimeTitle(AnimeTitle.Type.OFFICIAL, "eng", "official"),
                        new AnimeTitle(AnimeTitle.Type.SHORT, "eng", "short")
                ))
                .type(Anime.Type.SERIES)
                .episodeCount(4L)
                .startDate(LocalDate.of(1986, Month.SEPTEMBER, 24))
                .endDate(LocalDate.of(1986, Month.DECEMBER, 24))
                .deleted(true)
                .build();

        assertThat(strategy.changedAnime(List.of(anime), emptyList()), contains(deleted));
    }
}