package dmo.server.service;

import dmo.server.domain.User;
import dmo.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
@Slf4j
public class UserRegistrar {
    private final UserRepository userRepository;

    @Transactional
    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        log.info("On AuthenticationSuccessEvent: {}", event);

        var auth = event.getAuthentication();
        if (auth == null) {
            return;
        }

        var principal = auth.getPrincipal();
        if (!(principal instanceof OidcUser)) {
            log.warn("Principal {} is not OidcUser", principal);
            return;
        }

        var oidUser = (OidcUser) principal;
        if (!StringUtils.hasText(oidUser.getEmail())) {
            log.warn("No email: {}", oidUser.getClaims());
            return;
        }

        var user = userRepository.findById(oidUser.getEmail())
                .orElseGet(() -> {
                    var u = new User();
                    u.setEmail(oidUser.getEmail());
                    return userRepository.save(u);
                });

        log.info("Confirmed user: {}", user);
    }
}
