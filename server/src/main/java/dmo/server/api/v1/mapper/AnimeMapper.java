package dmo.server.api.v1.mapper;

import dmo.server.api.v1.dto.AnimeDto;
import dmo.server.api.v1.dto.PageDto;
import dmo.server.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AnimeMapper {

    //Anime fromDto(AnimeDto dto);

    AnimeDto toDto(Anime entity);

    List<AnimeDto> toDtoList(List<Anime> entities);

    default PageDto<AnimeDto> toPageDto(Page<Anime> page) {
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