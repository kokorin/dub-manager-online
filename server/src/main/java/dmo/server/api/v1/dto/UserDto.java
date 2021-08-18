package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDto {
    @ApiModelProperty(required = true, readOnly = true)
    private String email;
    @ApiModelProperty(required = true, readOnly = true)
    private String fullName;
    @ApiModelProperty(required = true, readOnly = true)
    private String givenName;
    @ApiModelProperty(required = true, readOnly = true)
    private String familyName;
    @ApiModelProperty(required = true, readOnly = true)
    private String middleName;
    @ApiModelProperty(required = true, readOnly = true)
    private String nickName;
    @ApiModelProperty(required = true, readOnly = true)
    private String preferredUsername;
    @ApiModelProperty(required = true, readOnly = true)
    private String picture;
    @ApiModelProperty(required = true, readOnly = true)
    private String locale;
}
