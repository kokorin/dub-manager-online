package dmo.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        SimpleUserDetails userDetails = (SimpleUserDetails) authentication.getPrincipal();
        String jwt = jwtService.toJwt(userDetails);

        long expiresIn = Duration.between(Instant.now(), userDetails.getExpiresAfter()).toMillis();
        String response = "{\n" +
                "   \"access_token\": \"" + jwt + "\",\n" +
                "   \"expires_in\": " + expiresIn + "\n" +
                "}\n";

        httpServletResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Cookie accessTokenCookie = new Cookie("access_token", jwt);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/api/");
        httpServletResponse.addCookie(accessTokenCookie);

        httpServletResponse.getWriter()
                .append(response)
                .flush();
    }
}
