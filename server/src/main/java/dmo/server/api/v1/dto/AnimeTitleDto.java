package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AnimeTitleDto {
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private AnimeTitleTypeDto type;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String lang;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String text;
}
