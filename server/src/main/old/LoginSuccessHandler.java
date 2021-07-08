package dmo.server.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @NonNull
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        var userDetails = (OAuth2User) authentication.getPrincipal();
        String jwt = jwtService.toJwt(userDetails);

        Cookie accessTokenCookie = new Cookie("access_token", jwt);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/api/");
        httpServletResponse.addCookie(accessTokenCookie);

        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
