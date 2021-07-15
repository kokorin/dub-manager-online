package dmo.server.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;

import java.time.Instant;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {
    private static final String SECRET = "oisaldfowieurlkg[010934lkfs;ld;0p'[q'a9374gkd, vfa]asdf";

    @Test
    void jwtServiceRequiresMinimumSecretLength() {
        var ex = assertThrows(RuntimeException.class, () -> new JwtService("bit"));
        assertThat(ex.getMessage(), containsString("Too short secret"));
    }

    @Test
    void jwtServiceIsGeneratedIfNullOrEmpty() {
        new JwtService(null);
        new JwtService("");
    }

    @Test
    void createAndParse() {
        var jwtService = new JwtService(SECRET);
        var user = new JwtUser(
                "username@mailserver.com",
                AuthorityUtils.NO_AUTHORITIES,
                "fullName",
                "givenName",
                "familyName",
                "middleName",
                "nickName",
                "preferredUsername",
                "picture",
                "locale"
        );
        var token = jwtService.createToken(user);
        var actualUser = jwtService.parseToken(token);

        assertEquals("username@mailserver.com", actualUser.getEmail());
        assertEquals(AuthorityUtils.NO_AUTHORITIES, actualUser.getAuthorities());
        assertEquals("fullName", actualUser.getFullName());
        assertEquals("givenName", actualUser.getGivenName());
        assertEquals("familyName", actualUser.getFamilyName());
        assertEquals("middleName", actualUser.getMiddleName());
        assertEquals("nickName", actualUser.getNickName());
        assertEquals("preferredUsername", actualUser.getPreferredUsername());
        assertEquals("picture", actualUser.getPicture());
        assertEquals("locale", actualUser.getLocale());
    }

    @Test
    void createAndParseWithAuthorities() {
        var jwtService = new JwtService(SECRET);
        var authorities = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_RANDOM");
        var user = new JwtUser(
                "username@mailserver.com",
                authorities,
                "fullName",
                "givenName",
                "familyName",
                "middleName",
                "nickName",
                "preferredUsername",
                "picture",
                "locale"
        );
        var token = jwtService.createToken(user);
        var actualUser = jwtService.parseToken(token);

        assertEquals("username@mailserver.com", actualUser.getEmail());
        assertEquals(Set.of("ROLE_USER", "ROLE_RANDOM"), AuthorityUtils.authorityListToSet(actualUser.getAuthorities()));
        assertEquals("fullName", actualUser.getFullName());
        assertEquals("givenName", actualUser.getGivenName());
        assertEquals("familyName", actualUser.getFamilyName());
        assertEquals("middleName", actualUser.getMiddleName());
        assertEquals("nickName", actualUser.getNickName());
        assertEquals("preferredUsername", actualUser.getPreferredUsername());
        assertEquals("picture", actualUser.getPicture());
        assertEquals("locale", actualUser.getLocale());
    }

    @Test
    void badCredentialsIsThrownIfSignedWithAnotherSecret() {
        var jwtService = new JwtService(SECRET);
        var user = new JwtUser(
                "username@mailserver.com",
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                "fullName",
                "givenName",
                "familyName",
                "middleName",
                "nickName",
                "preferredUsername",
                "picture",
                "locale"
        );
        var token = new JwtService(null).createToken(user);
        assertThrows(BadCredentialsException.class, () -> jwtService.parseToken(token));
    }

    @Test
    void badCredentialsIsThrownIfTokenIsExpired() {
        var jwtService = new JwtService(SECRET);
        var user = new JwtUser(
                "username@mailserver.com",
                AuthorityUtils.createAuthorityList("ROLE_USER"),
                "fullName",
                "givenName",
                "familyName",
                "middleName",
                "nickName",
                "preferredUsername",
                "picture",
                "locale"
        );
        var token = new JwtService(null).createToken(user, Instant.EPOCH);
        assertThrows(BadCredentialsException.class, () -> jwtService.parseToken(token));
    }
}
