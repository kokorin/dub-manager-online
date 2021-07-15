package dmo.server.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class SetAuthTokenUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @NonNull
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        var oidUser = (OidcUser) authentication.getPrincipal();
        var jwtUser = new JwtUser(oidUser);
        var token = jwtService.createToken(jwtUser);

        Cookie accessTokenCookie = new Cookie("access_token", token);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/api/");
        response.addCookie(accessTokenCookie);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
