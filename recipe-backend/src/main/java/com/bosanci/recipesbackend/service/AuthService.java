package com.bosanci.recipesbackend.service;

import com.bosanci.recipesbackend.dto.MessageResponse;
import com.bosanci.recipesbackend.dto.UserDto;
import com.bosanci.recipesbackend.dto.auth.*;
import com.bosanci.recipesbackend.exception.BadRequestException;
import com.bosanci.recipesbackend.exception.ForbiddenException;
import com.bosanci.recipesbackend.exception.ResourceNotFoundException;
import com.bosanci.recipesbackend.exception.UnauthorizedException;
import com.bosanci.recipesbackend.model.RevokedToken;
import com.bosanci.recipesbackend.model.Role;
import com.bosanci.recipesbackend.model.TokenType;
import com.bosanci.recipesbackend.model.User;
import com.bosanci.recipesbackend.repository.RevokedTokenRepository;
import com.bosanci.recipesbackend.repository.UserRepository;
import com.bosanci.recipesbackend.security.AuthenticatedUser;
import com.bosanci.recipesbackend.security.JwtService;
import com.bosanci.recipesbackend.security.JwtService.IssuedToken;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Duration ACTIVATION_LIFETIME = Duration.ofHours(24);
    private static final Duration RESET_LIFETIME = Duration.ofHours(1);

    private static final String BAD_CREDENTIALS = "Pogrešno korisničko ime/e-pošta ili lozinka.";

    private final UserRepository userRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final VerificationTokenService tokenService;
    private final EmailService emailService;
    private final UserMapper userMapper;

    /** Valid hash that no password matches. Checked when the user doesn't exist so timing doesn't reveal that. */
    private String dummyHash;

    @PostConstruct
    void init() {
        dummyHash = passwordEncoder.encode(UUID.randomUUID().toString());
    }

    // ---------------------------------------------------------------- registration

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        String username = request.username().trim();
        String email = normalizeEmail(request.email());

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new BadRequestException("Korisničko ime je već zauzeto.");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Nalog sa ovom e-poštom već postoji.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setEnabled(false);
        userRepository.save(user);

        emailService.sendActivation(user, tokenService.issue(user, TokenType.ACTIVATION, ACTIVATION_LIFETIME));
        return new MessageResponse("Registracija je uspešna. Proverite e-poštu i aktivirajte nalog.");
    }

    @Transactional
    public MessageResponse activate(String token) {
        User user = tokenService.redeem(token, TokenType.ACTIVATION);
        user.setEnabled(true);
        userRepository.save(user);
        return new MessageResponse("Nalog je aktiviran. Sada se možete prijaviti.");
    }

    @Transactional
    public MessageResponse resendActivation(String email) {
        userRepository.findByEmailIgnoreCase(normalizeEmail(email))
                .filter(user -> !user.isEnabled())
                .ifPresent(user -> emailService.sendActivation(user,
                        tokenService.issue(user, TokenType.ACTIVATION, ACTIVATION_LIFETIME)));
        // Same answer whether or not the address exists, so this can't be used to find registered e-mails.
        return new MessageResponse("Ako postoji neaktiviran nalog sa tom e-poštom, poslali smo novi link.");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String identifier = request.usernameOrEmail().trim();
        User user = (identifier.contains("@")
                ? userRepository.findByEmailIgnoreCase(identifier)
                : userRepository.findByUsernameIgnoreCase(identifier))
                .orElse(null);

        if (user == null) {
            passwordEncoder.matches(request.password(), dummyHash);
            throw new UnauthorizedException(BAD_CREDENTIALS);
        }
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException(BAD_CREDENTIALS);
        }
        if (!user.isEnabled()) {
            throw new ForbiddenException("Nalog još nije aktiviran. Proverite e-poštu ili zatražite novi link.");
        }
        return authResponse(user);
    }

    /** Stateless JWT cannot be "deleted", so its id is put on a block list until it would expire anyway. */
    @Transactional
    public void logout(AuthenticatedUser principal) {
        LocalDateTime now = LocalDateTime.now();
        revokedTokenRepository.deleteExpired(now);
        revokedTokenRepository.save(new RevokedToken(principal.tokenId(),
                LocalDateTime.ofInstant(principal.tokenExpiresAt(), ZoneId.systemDefault())));
    }

    // ---------------------------------------------------------------- current user

    @Transactional(readOnly = true)
    public UserDto me(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", userId));
    }

    // ---------------------------------------------------------------- helpers

    private AuthResponse authResponse(User user) {
        IssuedToken issued = jwtService.issue(user);
        return new AuthResponse(issued.token(), "Bearer", issued.expiresAt(), userMapper.toDto(user));
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}