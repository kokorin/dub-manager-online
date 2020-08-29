package dmo.server.api.v1.mapper;

import dmo.server.api.v1.dto.AnimeDto;
import dmo.server.api.v1.dto.AnimeLightDto;
import dmo.server.api.v1.dto.PageDto;
import dmo.server.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AnimeMapper {

    //Anime fromDto(AnimeDto dto);

    AnimeLightDto toLightDto(Anime entity);

    AnimeDto toDto(Anime entity);

    List<AnimeLightDto> toDtoList(List<Anime> entities);

    default PageDto<AnimeLightDto> toPageDto(Page<Anime> page) {
        return new PageDto<>(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                toDtoList(page.getContent())
        );
    }
}