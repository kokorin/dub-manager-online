package dmo.server.api.v1.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageDto<T> {
    @ApiModelProperty(required = true, readOnly = true)
    private int number;

    @ApiModelProperty(required = true, readOnly = true)
    private int size;

    @ApiModelProperty(required = true, readOnly = true)
    private int numberOfElements;

    @ApiModelProperty(required = true, readOnly = true)
    private int totalPages;

    @ApiModelProperty(required = true, readOnly = true)
    private long totalElements;

    @ApiModelProperty(required = true, readOnly = true)
    private List<T> content;
}
