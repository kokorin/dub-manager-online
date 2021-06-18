package dmo.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final GoogleAuthenticationService googleAuthenticationService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final JwtAuthenticationUserDetailsService preAuthenticatedUserDetailsService;
    private final DubUserDetailsService dubUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(googleAuthenticationProvider())
                .authenticationProvider(preAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                .addFilterAt(googleIdTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(authenticationFilter())

                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/api/openapi").permitAll()
                .antMatchers("/api/v1/conf").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()

                .and()

                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        ;
    }


    public Filter googleIdTokenFilter() throws Exception {
        GoogleAuthenticationProcessingFilter filter = new GoogleAuthenticationProcessingFilter("/login/google");

        // Required, not injected automatically
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);

        return filter;
    }

    @Bean
    public AuthenticationProvider googleAuthenticationProvider() {
        GoogleAuthenticationProvider provider = new GoogleAuthenticationProvider(
                googleAuthenticationService,
                dubUserDetailsService
        );
        return provider;
    }

    public Filter authenticationFilter() throws Exception {
        JwtPreAuthenticatedProcessingFilter filter = new JwtPreAuthenticatedProcessingFilter();

        // Required, not injected automatically
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }

    @Bean
    public AuthenticationProvider preAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(preAuthenticatedUserDetailsService);

        return provider;
    }
}
