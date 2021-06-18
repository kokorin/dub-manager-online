package dmo.server.security;

import dmo.server.domain.User;
import dmo.server.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class DubUserDetailsService {
    @NonNull
    private final UserRepository userRepository;

    @Transactional
    public DubUserDetails loadOrCreateUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User result = new User();
                    result.setEmail(email);
                    userRepository.save(result);
                    // Flushing to get user ID
                    userRepository.flush();
                    return result;
                });

        // TODO probably it's better to return a User here and convert User to DubUserDetails somewhere else
        return new DubUserDetails(
                user.getId(),
                user.getEmail(),
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                Instant.now().plus(1, ChronoUnit.DAYS)
        );
    }
}
