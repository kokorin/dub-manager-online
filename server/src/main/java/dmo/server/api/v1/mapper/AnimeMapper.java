package dmo.server.api.v1.mapper;

import dmo.server.api.v1.dto.*;
import dmo.server.domain.Anime;
import dmo.server.domain.AnimeStatus;
import dmo.server.domain.Episode;
import dmo.server.domain.EpisodeStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AnimeMapper {

    AnimeDto toAnimeDto(Anime entity);

    List<AnimeDto> toAnimeDtoList(List<Anime> entities);

    default PageDto<AnimeDto> toAnimePageDto(Page<Anime> page) {
        return new PageDto<>(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                toAnimeDtoList(page.getContent())
        );
    }

    Episode.Type toEpisodeType(EpisodeTypeDto typeDto);

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

    AnimeStatusDto toAnimeStatusDto(AnimeStatus entity);

    List<AnimeStatusDto> toAnimeStatusDtoList(List<AnimeStatus> entities);

    default PageDto<AnimeStatusDto> toAnimeStatusPageDto(Page<AnimeStatus> page) {
        return new PageDto<>(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                toAnimeStatusDtoList(page.getContent())
        );
    }

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "anime", ignore = true)
    @Mapping(target = "regularEpisodeCompleteCount", ignore = true)
    @Mapping(target = "regularEpisodeTotalCount", ignore = true)
    @Mapping(target = "regularEpisodeNextAirDate", ignore = true)
    void updateAnimeStatus(UpdateAnimeStatusDto update, @MappingTarget AnimeStatus entity);

    EpisodeStatusDto toEpisodeStatusDto(EpisodeStatus entity);

    List<EpisodeStatusDto> toEpisodeStatusDtoList(List<EpisodeStatus> entities);

    default PageDto<EpisodeStatusDto> toEpisodeStatusPageDto(Page<EpisodeStatus> page) {
        return new PageDto<>(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                toEpisodeStatusDtoList(page.getContent())
        );
    }

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "episode", ignore = true)
    void updateEpisodeStatus(UpdateEpisodeStatusDto update, @MappingTarget EpisodeStatus entity);
}