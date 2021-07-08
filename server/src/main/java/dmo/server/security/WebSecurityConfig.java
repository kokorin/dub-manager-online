package dmo.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

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

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SetAuthTokenUrlAuthenticationSuccessHandler();
    }

    private JwtIssuerAuthenticationManagerResolver authenticationManagerResolver() {
        return new JwtIssuerAuthenticationManagerResolver(
                "https://accounts.google.com"
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
