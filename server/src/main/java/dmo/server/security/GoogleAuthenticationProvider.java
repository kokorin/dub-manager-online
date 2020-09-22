package dmo.server.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class GoogleAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private GoogleIdTokenVerifier tokenVerifier;

    @Value("${google.client_id}")
    private String clientId;

    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public void afterPropertiesSet() throws Exception {
        HttpTransport httpTransport = new NetHttpTransport.Builder()
                .build();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        tokenVerifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        String idTokenString = (String) authentication.getPrincipal();

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
        System.out.println("User ID: " + userId);

        // Get profile information from payload
        String email = payload.getEmail();
        Boolean emailVerified = payload.getEmailVerified();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");

        UserDetails ud = new SimpleUserDetails(
                email,
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                Instant.now().plus(1, ChronoUnit.DAYS)
        );

        userDetailsChecker.check(ud);

        GoogleIdAuthenticationToken result = new GoogleIdAuthenticationToken(ud, ud.getAuthorities());
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GoogleIdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
