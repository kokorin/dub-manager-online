package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AnimeDto {
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private List<AnimeTitleDto> titles;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private AnimeTypeDto type;
}
