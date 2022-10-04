package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorDto {
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private final String message;
}
