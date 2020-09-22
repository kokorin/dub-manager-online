package dmo.server.security;

import dmo.server.domain.User;
import dmo.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class SimpleUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        //String role = user.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER";

        return new SimpleUserDetails(
                username,
                null,
                AuthorityUtils.NO_AUTHORITIES,
                Instant.now().plus(1, ChronoUnit.HOURS)
        );
    }
}
