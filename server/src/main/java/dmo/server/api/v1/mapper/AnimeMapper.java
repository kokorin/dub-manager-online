package dmo.server.api.v1.mapper;

import dmo.server.api.v1.dto.AnimeDto;
import dmo.server.api.v1.dto.AnimeLightDto;
import dmo.server.api.v1.dto.EpisodeDto;
import dmo.server.api.v1.dto.PageDto;
import dmo.server.domain.Anime;
import dmo.server.domain.Episode;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AnimeMapper {

    //Anime fromDto(AnimeDto dto);

    AnimeLightDto toAnimeLightDto(Anime entity);

    AnimeDto toAnimeDto(Anime entity);

    List<AnimeLightDto> toAnimeDtoList(List<Anime> entities);

    default PageDto<AnimeLightDto> toAnimePageDto(Page<Anime> page) {
        return new PageDto<>(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                toAnimeDtoList(page.getContent())
        );
    }

    EpisodeDto toEpisodeDto(Episode entity);

    List<EpisodeDto> toEpisodeDtoList(List<Episode> entities);

    default PageDto<EpisodeDto> toEpisodePageDto(Page<Episode> page) {
        return new PageDto<>(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                toEpisodeDtoList(page.getContent())
        );
    }
}