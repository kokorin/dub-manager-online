package dmo.server.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class DubUserDetails implements UserDetails {
    private final Long id;
    private final String email;
    private final List<? extends GrantedAuthority> authorities;
    private final Instant expiresAfter;
    private final String password = "N/A";

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
        return Instant.now().isBefore(expiresAfter);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
