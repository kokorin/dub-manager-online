package dmo.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;

public class DubUserDetails implements UserDetails {
    private final String email;
    private final List<? extends GrantedAuthority> authorities;
    private final Instant expiresAfter;

    public DubUserDetails(String email,
                          List<? extends GrantedAuthority> authorities,
                          Instant expiresAfter) {
        this.email = email;
        this.authorities = authorities;
        this.expiresAfter = expiresAfter;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return "N/A";
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Instant getExpiresAfter() {
        return expiresAfter;
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
