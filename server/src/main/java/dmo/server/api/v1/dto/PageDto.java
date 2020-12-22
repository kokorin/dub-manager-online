package dmo.server.api.v1.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class PageDto<T> {
    @ApiModelProperty(required = true)
    private final int number;

    @ApiModelProperty(required = true)
    private final int size;

    @ApiModelProperty(required = true)
    private final int numberOfElements;

    @ApiModelProperty(required = true)
    private final int totalPages;

    @ApiModelProperty(required = true)
    private final long totalElements;

    @ApiModelProperty(required = true)
    private final List<T> content;
}
