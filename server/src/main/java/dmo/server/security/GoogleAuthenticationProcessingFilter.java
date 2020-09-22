package dmo.server.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoogleAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public GoogleAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String tokenId = request.getParameter("id_token");
        if (StringUtils.isEmpty(tokenId)) {
            return null;
        }

        GoogleIdAuthenticationToken token = new GoogleIdAuthenticationToken(tokenId);

        return getAuthenticationManager().authenticate(token);
    }
}
