package dmo.server.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class JwtPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            return null;
        }

        String[] typeAndValue = authHeader.split(" ");
        if (typeAndValue.length != 2 || !"Bearer".equalsIgnoreCase(typeAndValue[0])) {
            return null;
        }

        return typeAndValue[1];
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}
