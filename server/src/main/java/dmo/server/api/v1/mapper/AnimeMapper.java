package dmo.server.api.v1.mapper;

import dmo.server.api.v1.dto.*;
import dmo.server.domain.Anime;
import dmo.server.domain.AnimeStatus;
import dmo.server.domain.Episode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    AnimeStatusDto.Status toAnimeStatusDtoEnum(AnimeStatus.Status entity);
    AnimeStatus.Status fromAnimeStatusDtoEnum(AnimeStatusDto.Status dto);

    @Mapping(target = "status", ignore = true)
    void updateAnimeStatus(Anime entity, @MappingTarget AnimeStatusDto dto);

    default void updateAnimeStatus(AnimeStatus entity, @MappingTarget AnimeStatusDto dto) {
        var status = toAnimeStatusDtoEnum(entity.getStatus());
        dto.setStatus(status);
    }

    default AnimeStatusDto toAnimeStatusDto(AnimeStatus entity) {
        if (entity == null) {
            return null;
        }

        var result = new AnimeStatusDto();
        updateAnimeStatus(entity.getAnime(), result);
        updateAnimeStatus(entity, result);

        return result;
    }

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
}