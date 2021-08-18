package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnimeStatusDto {
    @ApiModelProperty(required = true, readOnly = true)
    private AnimeDto anime;

    @ApiModelProperty(required = true, readOnly = true)
    private AnimeProgressDto progress;

    @ApiModelProperty(required = true, readOnly = true)
    private String comment;

    @ApiModelProperty(required = true, readOnly = true)
    private Long completedRegularEpisodes;

    @ApiModelProperty(required = true, readOnly = true)
    private Long totalRegularEpisodes;
}
