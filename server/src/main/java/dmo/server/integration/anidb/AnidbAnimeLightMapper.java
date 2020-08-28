package dmo.server.integration.anidb;

import dmo.server.domain.Anime;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AnidbAnimeLightMapper {
    @Mapping(target = "type", ignore = true)
    Anime toAnime(AnidbAnimeLight anidbAnimeLight);

    @AfterMapping
    default void toAnime(AnidbAnimeLight anidbAnimeLight, @MappingTarget Anime anime) {
        anime.setType(Anime.Type.UNKNOWN);
    }

    List<Anime> toAnimeList(List<AnidbAnimeLight> anidbAnimeLight);
}
