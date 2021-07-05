package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDto {
    @ApiModelProperty(required = true)
    private String googleOAuthClientId;
}
