package dmo.server.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AnidbProperties {
    public final String client;
    public final String clientVersion;

    public AnidbProperties(@Value("${dmo.anidb.client}") String client,
                           @Value("${dmo.anidb.client.version}") String clientVersion) {
        this.client = client;
        this.clientVersion = clientVersion;
    }
}
