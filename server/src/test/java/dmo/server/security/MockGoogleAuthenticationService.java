package dmo.server.security;

import dmo.server.prop.GoogleOAuthProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
@Primary
public class MockGoogleAuthenticationService /*extends GoogleAuthenticationService*/ {
    /*public MockGoogleAuthenticationService() {
        super(new GoogleOAuthProperties("Don't give anything"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public DubUserDetails fromIdToken(String idTokenString) {
        var email = idTokenString;

        return new DubUserDetails(
                null,
                email,
                AuthorityUtils.NO_AUTHORITIES,
                null
        );
    }*/
}
