package dmo.server.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.util.ByteUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * @see <a href="https://connect2id.com/products/nimbus-jose-jwt">nimbus-jose-jwt</a>
 */
@Slf4j
public class JwtService {
    private final JWSAlgorithm algorithm;
    private final MACSigner signer;
    private final MACVerifier verifier;
    private final Duration expiration = Duration.ofDays(1L);

    public JwtService(@Nullable String secret) {
        this(secret, JWSAlgorithm.HS256);
    }

    protected JwtService(String jwtKey, JWSAlgorithm algorithm) {
        this(getOrGenerateSecret(jwtKey, algorithm), algorithm);
    }

    protected JwtService(byte[] secret, JWSAlgorithm algorithm) {
        try {
            this.signer = new MACSigner(secret);
            this.verifier = new MACVerifier(secret);
            this.algorithm = algorithm;
        } catch (JOSEException e) {
            throw new IllegalArgumentException("Failed to create JwtService", e);
        }
    }

    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String FULL_NAME_CLAIM = "fullName";
    private static final String GIVEN_NAME_CLAIM = "givenName";
    private static final String FAMILY_NAME_CLAIM = "familyName";
    private static final String MIDDLE_NAME_CLAIM = "middleName";
    private static final String NICK_NAME_CLAIM = "nickName";
    private static final String PREFERRED_USERNAME_CLAIM = "preferredUsername";
    private static final String PICTURE_CLAIM = "picture";
    private static final String LOCALE_CLAIM = "locale";

    public String createToken(JwtUser user) {
        return createToken(user, Instant.now().plus(expiration));
    }

    String createToken(JwtUser user, Instant expirationTime) {
        log.debug("Generating JWT: {}", user);

        var header = new JWSHeader.Builder(algorithm)
                .type(JOSEObjectType.JWT)
                .build();

        var payload = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .claim(AUTHORITIES_CLAIM, AuthorityUtils.authorityListToSet(user.getAuthorities()))
                .claim(FULL_NAME_CLAIM, user.getFullName())
                .claim(GIVEN_NAME_CLAIM, user.getGivenName())
                .claim(FAMILY_NAME_CLAIM, user.getFamilyName())
                .claim(MIDDLE_NAME_CLAIM, user.getMiddleName())
                .claim(NICK_NAME_CLAIM, user.getNickName())
                .claim(PREFERRED_USERNAME_CLAIM, user.getPreferredUsername())
                .claim(PICTURE_CLAIM, user.getPicture())
                .claim(LOCALE_CLAIM, user.getLocale())
                .expirationTime(Date.from(expirationTime))
                .build();

        var signed = new SignedJWT(header, payload);
        try {
            signed.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to sing token", e);
        }

        return signed.serialize();
    }

    public JwtUser parseToken(String token) {
        final SignedJWT signed;
        try {
            signed = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new BadCredentialsException("Failed to parse token", e);
        }

        final boolean valid;
        try {
            valid = signed.verify(verifier);
        } catch (JOSEException e) {
            throw new BadCredentialsException("Failed to verify signature", e);
        }
        if (!valid) {
            throw new BadCredentialsException("Invalid signature");
        }

        final JWTClaimsSet claims;
        try {
            claims = signed.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new BadCredentialsException("Failed to parse claims", e);
        }

        if (claims == null) {
            throw new BadCredentialsException("No claims extracted");
        }

        var expired = claims.getExpirationTime().toInstant().isBefore(Instant.now());
        if (expired) {
            throw new CredentialsExpiredException("Credentials expired");
        }

        try {
            return new JwtUser(
                    claims.getSubject(),
                    AuthorityUtils.createAuthorityList(claims.getStringArrayClaim(AUTHORITIES_CLAIM)),
                    claims.getStringClaim(FULL_NAME_CLAIM),
                    claims.getStringClaim(GIVEN_NAME_CLAIM),
                    claims.getStringClaim(FAMILY_NAME_CLAIM),
                    claims.getStringClaim(MIDDLE_NAME_CLAIM),
                    claims.getStringClaim(NICK_NAME_CLAIM),
                    claims.getStringClaim(PREFERRED_USERNAME_CLAIM),
                    claims.getStringClaim(PICTURE_CLAIM),
                    claims.getStringClaim(LOCALE_CLAIM)
            );
        } catch (ParseException | NullPointerException e) {
            throw new BadCredentialsException("Failed to parse claims", e);
        }
    }

    private static byte[] getOrGenerateSecret(String secret, JWSAlgorithm algorithm) {
        final int minBitsLength;
        try {
            minBitsLength = MACSigner.getMinRequiredSecretLength(algorithm);
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to calculate secret min length", e);
        }

        byte[] bytes = null;
        if (StringUtils.hasText(secret)) {
            bytes = secret.getBytes(StandardCharsets.UTF_8);

            if (bytes.length < ByteUtils.byteLength(minBitsLength)) {
                throw new RuntimeException("Too short secret, min length is " + minBitsLength);
            }
        }

        if (bytes == null) {
            log.warn("Generating random secret, consider setting static key for production");
            bytes = new byte[64];
            new SecureRandom().nextBytes(bytes);
        }

        return bytes;
    }
}
