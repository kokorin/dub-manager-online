package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorDto {
    @ApiModelProperty(required = true, readOnly = true)
    private final String message;
}
