package dmo.server.integration.anidb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("dmo.anidb")
@Getter
@Setter
public class AnidbProperties {
    private String client;
    private String clientVersion;
}
