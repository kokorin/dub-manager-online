package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EpisodeDto {
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Long animeId;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Long number;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private List<EpisodeTitleDto> titles;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private EpisodeTypeDto type;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Long length;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate airDate;
}
