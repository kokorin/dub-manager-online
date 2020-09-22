package dmo.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final JwtService jwtService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String jwt = (String) token.getPrincipal();
        return jwtService.fromJwt(jwt);
    }
}
