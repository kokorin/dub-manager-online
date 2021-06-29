package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnimeStatusDto {
    @ApiModelProperty(required = true)
    private AnimeDto anime;

    @ApiModelProperty(required = true)
    private AnimeProgressDto progress;

    @ApiModelProperty(required = true)
    private String comment;

    @ApiModelProperty(required = true)
    private Long completedRegularEpisodes;

    @ApiModelProperty(required = true)
    private Long totalRegularEpisodes;
}
