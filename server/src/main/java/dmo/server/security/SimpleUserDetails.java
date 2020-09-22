package dmo.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;

public class SimpleUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final List<? extends GrantedAuthority> authorities;
    private final Instant expiresAfter;

    public SimpleUserDetails(String username, String password, List<? extends GrantedAuthority> authorities, Instant expiresAfter) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.expiresAfter = expiresAfter;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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
