package com.bosanci.recipesbackend.security;

import com.bosanci.recipesbackend.config.AppProperties;
import com.bosanci.recipesbackend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {

    private static final int MIN_SECRET_BYTES = 32;

    private final SecretKey key;
    private final Duration lifetime;

    public JwtService(AppProperties properties) {
        byte[] secret = properties.jwt().secret().getBytes(StandardCharsets.UTF_8);
        if (secret.length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "app.jwt.secret mora imati najmanje " + MIN_SECRET_BYTES + " karaktera (postavi JWT_SECRET).");
        }
        this.key = Keys.hmacShaKeyFor(secret);
        this.lifetime = Duration.ofMinutes(properties.jwt().expirationMinutes());
    }

    public IssuedToken issue(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(lifetime);
        String token = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .claim("ver", user.getTokenVersion())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
        return new IssuedToken(token, expiresAt);
    }

    /** Returns empty for anything that is not a valid, unexpired token signed by this server. */
    public Optional<ParsedToken> parse(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            Number version = claims.get("ver", Number.class);
            return Optional.of(new ParsedToken(
                    Long.parseLong(claims.getSubject()),
                    claims.getId(),
                    claims.getExpiration().toInstant(),
                    version == null ? 0 : version.intValue()));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public record IssuedToken(String token, Instant expiresAt) {
    }

    public record ParsedToken(Long userId, String tokenId, Instant expiresAt, int version) {
    }
}
