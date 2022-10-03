package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EpisodeStatusDto {
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private EpisodeDto episode;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private EpisodeProgressDto progress;
}
