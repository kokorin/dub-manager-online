package dmo.server.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthProperties {
    public final String clientId;

    public GoogleOAuthProperties(@Value("${google.oauth.client.id}") String clientId) {
        this.clientId = clientId;
    }
}
