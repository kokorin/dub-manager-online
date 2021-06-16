package dmo.server.integration.anidb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AnidbProperties {
    @Value("${dmo.anidb.client}")
    private String client;
    @Value("${dmo.anidb.client.version}")
    private String clientVersion;
}
