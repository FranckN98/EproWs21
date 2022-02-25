package de.thbingen.epro.security;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
public class JWTTokenService implements Clock, TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt..type}")
    private String jwtType;
    @Value("${jwt.audience}")
    private String jwtAudience;

    JWTTokenService() {
        super();
    }

    @Override
    public String newToken(final Map<String, String> attributes) {
        final DateTime now = DateTime.now();
        final Claims claims = Jwts.claims()
                .setIssuer(jwtIssuer)
                .setSubject(attributes.get("username"))
                .setIssuedAt(now.toDate())
                .setAudience(jwtAudience)
                .setExpiration(new Date(System.currentTimeMillis()+3600000));
                claims.putAll(attributes);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(HS256, jwtSecret)
                .compact();
    }

    @Override
    public Map<String, String> verify(final String token) {
        final JwtParser parser = Jwts.parser().requireIssuer(jwtIssuer).setClock(this).setSigningKey(jwtSecret);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
        try {
            final Claims claims = toClaims.get();
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            for (final Map.Entry<String, Object> e : claims.entrySet()) {
                builder.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return builder.build();
        } catch (final IllegalArgumentException | JwtException e) {
            return ImmutableMap.of();
        }
    }

    @Override
    public Date now() {
        final DateTime now = DateTime.now();
        return now.toDate();
    }
}
