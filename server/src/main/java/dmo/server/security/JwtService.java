package dmo.server.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtService {
    @Value("${jwt.key:}")
    private String jwtKey;

    private Key signKey;

    private JwtParser jwtParser;

    private static final String AUTHORITIES_CLAIM = "authorities";

    @PostConstruct
    public void postConstruct() {
        if (StringUtils.isEmpty(jwtKey)) {
            signKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            log.warn("Using generated JWT key, consider setting it");
        } else {
            signKey = Keys.hmacShaKeyFor(jwtKey.getBytes());
        }
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(signKey)
                .build();
    }

    public String toJwt(SimpleUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(userDetails.getExpiresAfter()))
                .claim(
                        AUTHORITIES_CLAIM,
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .signWith(signKey)
                .compact();
    }

    public SimpleUserDetails fromJwt(String jwt) {
        Claims claims;

        try {
            claims = jwtParser.parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("User credentials have expired", e);
        } catch (JwtException e) {
            throw new BadCredentialsException("User credentials are incorrect", e);
        }

        @SuppressWarnings("unchecked")
        SimpleUserDetails result = new SimpleUserDetails(
                claims.getSubject(),
                "N/A",
                ((List<String>) claims.get(AUTHORITIES_CLAIM, List.class)).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()),
                claims.getExpiration().toInstant()
        );

        return result;
    }
}
