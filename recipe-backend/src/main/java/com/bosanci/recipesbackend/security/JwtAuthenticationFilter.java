package com.bosanci.recipesbackend.security;

import com.bosanci.recipesbackend.model.User;
import com.bosanci.recipesbackend.repository.RevokedTokenRepository;
import com.bosanci.recipesbackend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RevokedTokenRepository revokedTokenRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository,
                                   RevokedTokenRepository revokedTokenRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.revokedTokenRepository = revokedTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticate(header.substring(BEARER_PREFIX.length()).trim());
        }
        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        jwtService.parse(token).ifPresent(parsed -> {
            if (revokedTokenRepository.existsById(parsed.tokenId())) {
                return;
            }
            User user = userRepository.findById(parsed.userId()).orElse(null);
            if (user == null || !user.isEnabled() || user.getTokenVersion() != parsed.version()) {
                return;
            }
            AuthenticatedUser principal = new AuthenticatedUser(
                    user.getId(), user.getUsername(), user.getRole(), parsed.tokenId(), parsed.expiresAt());
            var authentication = new UsernamePasswordAuthenticationToken(
                    principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }
}
