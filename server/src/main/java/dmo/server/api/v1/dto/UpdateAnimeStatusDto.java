package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateAnimeStatusDto {
    @Schema(required = true)
    private AnimeProgressDto progress;

    @Schema(required = true)
    private String comment;
}
