package dmo.server.api.v1.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAnimeStatusDto {
    @NotNull
    private Long animeId;

    @NotNull
    private AnimeStatusDto.Status status;
}
