package dmo.server.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class OidcUserToJwtUserConverter implements Converter<OidcUser, JwtUser> {
    @Override
    public JwtUser convert(OidcUser oidc) {
        return new JwtUser(
                oidc.getEmail(),
                oidc.getAuthorities(),
                oidc.getFullName(),
                oidc.getGivenName(),
                oidc.getFamilyName(),
                oidc.getMiddleName(),
                oidc.getNickName(),
                oidc.getPreferredUsername(),
                oidc.getPicture(),
                oidc.getLocale()
        );
    }
}
