package dmo.server.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;

@RequiredArgsConstructor
@Slf4j
public class GoogleAuthenticationProvider implements AuthenticationProvider {

    @NonNull
    private final GoogleAuthenticationService googleAuthenticationService;
    @NonNull
    private final DubUserDetailsService userDetailsService;
    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        var idTokenString = (String) authentication.getPrincipal();
        log.debug("Verifying ID token");
        var ud = googleAuthenticationService.fromIdToken(idTokenString);
        log.debug("ID token verified");
        ud = userDetailsService.loadOrCreateUserByEmail(ud.getEmail());

        userDetailsChecker.check(ud);
        log.info("User allowed to login: {}", ud);

        GoogleIdAuthenticationToken result = new GoogleIdAuthenticationToken(ud, ud.getAuthorities());
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GoogleIdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
