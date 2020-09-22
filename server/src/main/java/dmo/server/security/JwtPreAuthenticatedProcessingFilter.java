package dmo.server.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class JwtPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.isEmpty(authHeader)) {
            String[] typeAndValue = authHeader.split(" ");
            if (typeAndValue.length != 2 || !"Bearer".equalsIgnoreCase(typeAndValue[0])) {
                return null;
            }

            return typeAndValue[1];
        }

        String authCookieValue = Arrays.stream(request.getCookies())
                .filter(cookie -> "access_token".equals(cookie.getName()))
                .findAny()
                .map(Cookie::getValue)
                .orElse(null);

        return authCookieValue;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}
