package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EpisodeStatusDto {
    @ApiModelProperty(required = true, readOnly = true)
    private EpisodeDto episode;

    @ApiModelProperty(required = true, readOnly = true)
    private EpisodeProgressDto progress;
}
