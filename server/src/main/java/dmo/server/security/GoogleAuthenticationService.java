package dmo.server.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleAuthenticationService implements InitializingBean {

    private GoogleIdTokenVerifier tokenVerifier;

    @Value("${google.client_id}")
    private String clientId;

    @Override
    public void afterPropertiesSet() throws Exception {
        HttpTransport httpTransport = new NetHttpTransport.Builder()
                .build();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        tokenVerifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public DubUserDetails fromIdToken(String idTokenString) {
        final GoogleIdToken idToken;

        try {
            idToken = tokenVerifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to parse Google ID token", e);
        }

        if (idToken == null) {
            return null;
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        // Print user identifier
        String userId = payload.getSubject();

        // Get profile information from payload
        String email = payload.getEmail();
        Boolean emailVerified = payload.getEmailVerified();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");

        return new DubUserDetails(
                null,
                email,
                AuthorityUtils.NO_AUTHORITIES,
                null
        );
    }

}
