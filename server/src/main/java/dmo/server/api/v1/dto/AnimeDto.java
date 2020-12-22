package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AnimeDto {
    @ApiModelProperty(required = true)
    private Long id;

    @ApiModelProperty(required = true)
    private List<AnimeTitleDto> titles;

    @ApiModelProperty(required = true,
            allowableValues = "MOVIE, OVA, TV_SERIES, TV_SPECIAL, WEB, MUSIC_VIDEO, OTHER, UNKNOWN")
    private String type;
}
