package dmo.server.integration.anidb.prop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(value = "integration.anidb.titles")
@RequiredArgsConstructor
public class AnidbTitlesProperties {
    public final String baseUrl;
    public final Duration updateInterval;
    public final Duration checkInterval;
}
