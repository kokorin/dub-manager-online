package dmo.server.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDto {
    @ApiModelProperty(required = true)
    private String email;
    private String fullName;
    private String givenName;
    private String familyName;
    private String middleName;
    private String nickName;
    private String preferredUsername;
    private String picture;
    private String locale;
}
