package dmo.server.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final String jwtKey;

    public WebSecurityConfig(@Value("${jwt.key:}") String jwtKey) {
        this.jwtKey = jwtKey;
    }

    private GrantedAuthoritiesMapper authoritiesMapper() {
        var result = new SimpleAuthorityMapper();
        result.setDefaultAuthority("ROLE_USER");
        return result;
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService(jwtKey);
    }

    @Bean
    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preAuthenticatedUserDetailsService() {
        return new JwtAuthenticationUserDetailsService(jwtService());
    }

    @Bean
    public AuthenticationProvider preAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(preAuthenticatedUserDetailsService());
        return provider;
    }

    @Bean
    public AbstractPreAuthenticatedProcessingFilter preAuthenticatedProcessingFilter() throws Exception {
        var filter = new JwtPreAuthenticatedProcessingFilter();
        // Required, not injected automatically
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SetAuthTokenUrlAuthenticationSuccessHandler(jwtService());
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

                .addFilter(preAuthenticatedProcessingFilter())
        ;
    }
}
