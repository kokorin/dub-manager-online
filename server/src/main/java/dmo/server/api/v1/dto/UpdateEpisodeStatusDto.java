package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateEpisodeStatusDto {
    @ApiModelProperty(required = true)
    private EpisodeProgressDto progress;
}
