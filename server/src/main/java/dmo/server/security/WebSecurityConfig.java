package dmo.server.security;

import lombok.RequiredArgsConstructor;
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
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.Collections;
import java.util.function.Function;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
        return new TrustedIssuerJwtAuthenticationManagerResolver(
                Collections.singleton("https://accounts.google.com")::contains,
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
