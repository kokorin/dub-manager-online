package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnimeTitleDto {
    @ApiModelProperty(required = true, readOnly = true,
            allowableValues = "SHORT, OFFICIAL, SYNONYM, MAIN, CARD, KANA")
    private String type;

    @ApiModelProperty(required = true, readOnly = true)
    private String lang;

    @ApiModelProperty(required = true, readOnly = true)
    private String text;
}
