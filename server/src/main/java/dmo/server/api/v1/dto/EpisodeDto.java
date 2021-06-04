package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EpisodeDto {
    @ApiModelProperty(required = true)
    private Long id;

    @ApiModelProperty(required = true)
    private Long number;

    @ApiModelProperty(required = true)
    private List<EpisodeTitleDto> titles;

    @ApiModelProperty(required = true,
            allowableValues = "REGULAR, SPECIAL, CREDIT, TRAILER, PARODY, OTHER")
    private String type;

    @ApiModelProperty(required = true)
    private Long length;

    @ApiModelProperty(required = true)
    private LocalDate airDate;
}
