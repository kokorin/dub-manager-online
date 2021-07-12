package dmo.server.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Holds a list of trusted Jwt/OpenID issuers.
 */
@Slf4j
@RequiredArgsConstructor
public class TrustedIssuerJwtAuthenticationManagerResolver implements AuthenticationManagerResolver<String> {

    private final Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();

    private final Predicate<String> trustedIssuer;
    private final Function<String, AuthenticationManager> authManagerProducer;

    @Override
    public AuthenticationManager resolve(String issuer) {
        if (this.trustedIssuer.test(issuer)) {
            AuthenticationManager authenticationManager = this.authenticationManagers.computeIfAbsent(issuer, authManagerProducer);
            log.debug("Resolved AuthenticationManager for issuer '{}'", issuer);
            return authenticationManager;
        }
        else {
            log.debug("Did not resolve AuthenticationManager since issuer is not trusted");
        }
        return null;
    }
}
