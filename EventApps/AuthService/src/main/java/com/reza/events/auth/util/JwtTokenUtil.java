package com.reza.events.auth.util;

import com.reza.events.security.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry-second:3600}")
    private long expirySeconds;

    @Value("${jwt.issuer:https://events.reza.com}")
    private String issuer;

    public String generateToken(AuthenticatedUser user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expirySeconds);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("firstName", user.getFirstName());
        claims.put("lastNam" + "e", user.getLastName());
        claims.put("roles", user.getRoles());

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claims(claims)
                .signWith(buildSigningKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(buildSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractSubject(String token) {
        return parseToken(token).getSubject();
    }

    private SecretKey buildSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public long getExpirySeconds(){
        return expirySeconds;
    }
}