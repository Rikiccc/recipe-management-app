package com.bosanci.recipesbackend.service;

import com.bosanci.recipesbackend.exception.BadRequestException;
import com.bosanci.recipesbackend.model.TokenType;
import com.bosanci.recipesbackend.model.User;
import com.bosanci.recipesbackend.model.VerificationToken;
import com.bosanci.recipesbackend.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final VerificationTokenRepository repository;

    @Transactional
    public String issue(User user, TokenType type, Duration lifetime) {
        repository.deleteAllForUser(user, type);

        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        repository.save(VerificationToken.builder()
                .tokenHash(hash(token))
                .user(user)
                .type(type)
                .expiresAt(LocalDateTime.now().plus(lifetime))
                .build());
        return token;
    }

    @Transactional
    public User redeem(String token, TokenType type) {
        VerificationToken stored = repository.findByHashAndType(hash(token), type)
                .orElseThrow(() -> new BadRequestException("Link je neispravan ili je već iskorišćen."));

        if (repository.deleteByTokenId(stored.getId()) == 0) {
            throw new BadRequestException("Link je neispravan ili je već iskorišćen.");
        }
        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Link je istekao. Zatražite novi.");
        }
        return stored.getUser();
    }

    private static String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
