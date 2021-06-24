package dmo.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dmo.server.security.DubUserDetails;
import lombok.Data;

@Data
public class JwtResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonIgnore
    private DubUserDetails userDetails;
}
