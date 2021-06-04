package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AnimeStatusDto {
    @ApiModelProperty(required = true)
    private AnimeDto anime;

    @ApiModelProperty(required = true)
    private AnimeProgressDto progress;
}
