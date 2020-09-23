package dmo.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;

@RequiredArgsConstructor
public class GoogleAuthenticationProvider implements AuthenticationProvider {

    private final GoogleAuthenticationService googleAuthenticationService;
    private final DubUserDetailsService userDetailsService;
    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        var idTokenString = (String) authentication.getPrincipal();
        var ud = googleAuthenticationService.fromIdToken(idTokenString);
        ud = userDetailsService.loadOrCreateUserByEmail(ud.getEmail());

        userDetailsChecker.check(ud);

        GoogleIdAuthenticationToken result = new GoogleIdAuthenticationToken(ud, ud.getAuthorities());
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GoogleIdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
