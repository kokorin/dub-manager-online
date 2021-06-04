package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnimeTitleDto {
    @ApiModelProperty(required = true,
            allowableValues = "SHORT, OFFICIAL, SYNONYM, MAIN, CARD, KANA")
    private String type;

    @ApiModelProperty(required = true)
    private String lang;

    @ApiModelProperty(required = true)
    private String text;
}
