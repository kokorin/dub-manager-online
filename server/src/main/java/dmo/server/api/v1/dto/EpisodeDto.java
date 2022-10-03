package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EpisodeDto {
    @ApiModelProperty(required = true, readOnly = true)
    private Long id;

    @ApiModelProperty(required = true, readOnly = true)
    private Long animeId;

    @ApiModelProperty(required = true, readOnly = true)
    private Long number;

    @ApiModelProperty(required = true, readOnly = true)
    private List<EpisodeTitleDto> titles;

    @ApiModelProperty(required = true, readOnly = true)
    private EpisodeTypeDto type;

    @ApiModelProperty(required = true, readOnly = true)
    private Long length;

    @ApiModelProperty(required = true, readOnly = true)
    private LocalDate airDate;
}
