package dmo.server.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GoogleIdAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;

    public GoogleIdAuthenticationToken(String idToken) {
        this(idToken, null);
    }

    public GoogleIdAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "N/A";
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
