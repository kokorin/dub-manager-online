package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDto {
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String email;
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String fullName;
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String givenName;
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String familyName;
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String middleName;
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String nickName;
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String preferredUsername;
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String picture;
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String locale;
}
