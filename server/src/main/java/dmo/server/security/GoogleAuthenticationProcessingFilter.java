package dmo.server.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class GoogleAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public GoogleAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.debug("Processing request: {}", request.getRequestURI());
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String tokenId = request.getParameter("id_token");
        if (StringUtils.isEmpty(tokenId)) {
            log.info("Empty id_token in request: {}", request.getRequestURI());
            return null;
        }

        GoogleIdAuthenticationToken token = new GoogleIdAuthenticationToken(tokenId);

        log.debug("Token found, proceeding authentication");
        return getAuthenticationManager().authenticate(token);
    }
}
