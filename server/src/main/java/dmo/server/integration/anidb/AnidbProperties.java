package dmo.server.integration.anidb;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("dmo.anidb")
@Getter
public class AnidbProperties {
    private String client;
    private String clientVersion;
}
