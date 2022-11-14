package dmo.server.integration.anidb.mapper;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.domain.Episode;
import dmo.server.integration.anidb.dto.AnidbAnime;
import dmo.server.integration.anidb.dto.AnidbAnimeTitle;
import dmo.server.integration.anidb.dto.AnidbEpisode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AnidbAnimeMapper {

    default Set<AnimeTitle> toAnimeTitles(Set<AnidbAnimeTitle> anidbTitles) {
        var knownTypes = EnumSet.of(AnidbAnimeTitle.Type.OFFICIAL, AnidbAnimeTitle.Type.SHORT);
        return anidbTitles.stream()
                .filter(title -> knownTypes.contains(title.type))
                .map(this::toAnimeTitle)
                .collect(Collectors.toSet());
    }

    AnimeTitle toAnimeTitle(AnidbAnimeTitle anidbTitle);

    default AnimeTitle.Type toAnimeTitleType(AnidbAnimeTitle.Type type) {
        return switch (type) {
            case SHORT -> AnimeTitle.Type.SHORT;
            case OFFICIAL -> AnimeTitle.Type.OFFICIAL;
            default -> throw new RuntimeException("Anime type should be skipped from list: " + type);
        };
    }

    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "externalSystem", constant = "ANIDB")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "lastUpdate", ignore = true)
    Anime toAnime(AnidbAnime anidbAnime);

    default Anime.Type toAnimeType(AnidbAnime.Type type) {
        return switch (type) {
            case MOVIE -> Anime.Type.MOVIE;
            case TV_SERIES -> Anime.Type.SERIES;
            case MUSIC_VIDEO -> Anime.Type.MUSIC;
            default -> Anime.Type.UNKNOWN;
        };
    }

    @Mapping(target = "externalId", source = "anidbEpisode.id")
    Episode toEpisode(AnidbEpisode anidbEpisode, Long animeId);

    default List<Episode> toEpisodeList(List<AnidbEpisode> anidbEpisodes, Long animeId) {
        return anidbEpisodes.stream()
                .map(ep -> toEpisode(ep, animeId))
                .collect(Collectors.toList());
    }
}
