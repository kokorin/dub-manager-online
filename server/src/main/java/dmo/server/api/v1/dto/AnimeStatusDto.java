package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AnimeStatusDto {
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private AnimeDto anime;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private AnimeProgressDto progress;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String comment;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Long regularEpisodeCompleteCount;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Long regularEpisodeTotalCount;

    @Schema(required = false, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate regularEpisodeNextAirDate;
}
