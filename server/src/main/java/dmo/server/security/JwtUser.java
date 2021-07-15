package dmo.server.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
@ToString(of = "email")
public class JwtUser implements UserDetails {

    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String fullName;
    private final String givenName;
    private final String familyName;
    private final String middleName;
    private final String nickName;
    private final String preferredUsername;
    private final String picture;
    private final String locale;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return "N/A";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
