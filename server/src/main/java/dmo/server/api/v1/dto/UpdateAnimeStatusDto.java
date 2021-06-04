package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAnimeStatusDto {
    @NotNull
    @ApiModelProperty(required = true)
    private AnimeProgressDto progress;
}
