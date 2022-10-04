package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateEpisodeStatusDto {
    @Schema(required = true)
    private EpisodeProgressDto progress;
}
