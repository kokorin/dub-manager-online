package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EpisodeTitleDto {
    @ApiModelProperty(required = true, readOnly = true)
    private String lang;

    @ApiModelProperty(required = true, readOnly = true)
    private String text;
}
