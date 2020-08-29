package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import dmo.server.domain.AnimeTitle;
import dmo.server.domain.Episode;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AnidbAnimeMapper {
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "episodeCount", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    Anime toAnime(AnidbAnimeLight anidbAnimeLight);

    @Mapping(target = "type", ignore = true)
    AnimeTitle toAnimeTitle(AnidbAnimeTitle anidbTitle);

    @AfterMapping
    default void toAnime(AnidbAnimeTitle anidbTitle, @MappingTarget AnimeTitle title) {
        AnidbAnimeTitle.Type type = anidbTitle.type;
        // AniDB responds with "syn" type for anime list request and with "synonym" for anime request
        if (anidbTitle.type == AnidbAnimeTitle.Type.SYN) {
            type = AnidbAnimeTitle.Type.SYNONYM;
        }

        AnimeTitle.Type targetType = AnimeTitle.Type.valueOf(type.name());
        title.setType(targetType);
    }

    List<Anime> toAnimeList(List<AnidbAnimeLight> anidbAnimeLight);

    Anime toAnime(AnidbAnime anidbAnime);

    @Mapping(target = "anime", ignore = true)
    Episode toEpisode(AnidbEpisode anidbEpisode);

    List<Episode> toEpisodeList(List<AnidbEpisode> anidbEpisodes);
}
