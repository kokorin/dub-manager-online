package dmo.server.security;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;

import java.time.Instant;

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
        var user = new JwtUser("username@mailserver.com", AuthorityUtils.NO_AUTHORITIES);
        var token = jwtService.createToken(user);
        var actualUser = jwtService.parseToken(token);

        assertEquals("username@mailserver.com", actualUser.getEmail());
        assertEquals(AuthorityUtils.NO_AUTHORITIES, actualUser.getAuthorities());
    }

    @Test
    void createAndParseWithAuthorities() {
        var jwtService = new JwtService(SECRET);
        var authorities = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_RANDOM");
        var user = new JwtUser("username@mailserver.com", authorities);
        var token = jwtService.createToken(user);
        var actualUser = jwtService.parseToken(token);

        assertEquals("username@mailserver.com", actualUser.getEmail());
        assertEquals(AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_RANDOM"),actualUser.getAuthorities());
    }

    @Test
    void badCredentialsIsThrownIfSignedWithAnotherSecret() {
        var jwtService = new JwtService(SECRET);
        var user = new JwtUser("username@mailserver.com", AuthorityUtils.createAuthorityList("ROLE_USER"));
        var token = new JwtService(null).createToken(user);
        assertThrows(BadCredentialsException.class, () -> jwtService.parseToken(token));
    }

    @Test
    void badCredentialsIsThrownIfTokenIsExpired() {
        var jwtService = new JwtService(SECRET);
        var user = new JwtUser("username@mailserver.com", AuthorityUtils.createAuthorityList("ROLE_USER"));
        var token = new JwtService(null).createToken(user, Instant.EPOCH);
        assertThrows(BadCredentialsException.class, () -> jwtService.parseToken(token));
    }
}
