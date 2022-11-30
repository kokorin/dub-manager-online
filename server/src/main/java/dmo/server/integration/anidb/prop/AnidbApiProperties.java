package dmo.server.integration.anidb.prop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(value = "integration.anidb.api")
@RequiredArgsConstructor
public class AnidbApiProperties {
    public final String baseUrl;
    public final Duration updateInterval;
    public final String clientName;
    public final String clientVersion;

}
