package dmo.server.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2ClientProperties oAuth2ClientProperties;

    private GrantedAuthoritiesMapper authoritiesMapper() {
        var result = new SimpleAuthorityMapper();
        result.setDefaultAuthority("ROLE_USER");
        return result;
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return new OidcUserAuthenticationConverter();
    }

    private Function<String, AuthenticationManager> authManagerProducer() {
        return issuer -> {
            var jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);
            var provider = new JwtAuthenticationProvider(jwtDecoder);
            provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
            return provider::authenticate;
        };
    }

    private AuthenticationManagerResolver<String> issuerAuthenticationManagerResolver() {
        var registeredProviders = oAuth2ClientProperties.getProvider();

        var trustedIssuers = new HashSet<String>();
        for (var entry : oAuth2ClientProperties.getRegistration().entrySet()) {
            var registrationId = entry.getKey();
            var registration = entry.getValue();

            var providerId = registration.getProvider();
            if (providerId == null) {
                providerId = registrationId;
            }
            if (providerId == null) {
                log.warn("Failed to detect OAuth2 Provider: {}", registration);
                continue;
            }

            String issuerUri = null;

            var provider = registeredProviders.get(providerId);
            if (provider != null) {
                issuerUri = provider.getIssuerUri();
            }
            if (issuerUri == null) {
                switch (registrationId.toLowerCase()) {
                    case "google":
                        issuerUri = "https://accounts.google.com";
                        break;
                    default:
                        log.warn("Unknown common provider Issuer URI: {}", registrationId);
                        break;
                }
            }
            if (issuerUri == null) {
                log.warn("Failed to find provider by ID: {}", providerId);
                continue;
            }

            trustedIssuers.add(issuerUri);
        }

        log.info("Trusted OAuth2 issuers: {}", trustedIssuers);

        return new TrustedIssuerJwtAuthenticationManagerResolver(
                trustedIssuers::contains,
                authManagerProducer()
        );
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SetAuthTokenUrlAuthenticationSuccessHandler();
    }

    private JwtIssuerAuthenticationManagerResolver authenticationManagerResolver() {
        return new JwtIssuerAuthenticationManagerResolver(
                issuerAuthenticationManagerResolver()
        );
    }

    private BearerTokenResolver bearerTokenResolver() {
        return new CookieBearerTokenResolver();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeRequests(requests -> requests
                        .antMatchers("/api/openapi").permitAll()
                        .antMatchers("/api/v1/conf").permitAll()
                        .antMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )

                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )

                .oauth2Login(login -> login
                        .userInfoEndpoint(info -> info
                                .userAuthoritiesMapper(authoritiesMapper())
                        )
                        .successHandler(authenticationSuccessHandler())
                )

                .oauth2ResourceServer(resourceServer -> resourceServer
                        .bearerTokenResolver(bearerTokenResolver())
                        .authenticationManagerResolver(authenticationManagerResolver())
                )
        ;
    }
}
