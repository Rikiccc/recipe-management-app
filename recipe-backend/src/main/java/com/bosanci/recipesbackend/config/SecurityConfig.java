package com.bosanci.recipesbackend.config;

import com.bosanci.recipesbackend.model.Role;
import com.bosanci.recipesbackend.repository.RevokedTokenRepository;
import com.bosanci.recipesbackend.repository.UserRepository;
import com.bosanci.recipesbackend.security.JwtAuthenticationFilter;
import com.bosanci.recipesbackend.security.JwtService;
import com.bosanci.recipesbackend.security.RestSecurityErrorHandlers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppProperties properties;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final RestSecurityErrorHandlers errorHandlers;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // no cookies/session, token is sent in header
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(errorHandlers)
                        .accessDeniedHandler(errorHandlers))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // authentication: only these need a token, the rest (login, register...) is public
                        .requestMatchers("/api/auth/logout", "/api/auth/change-password", "/api/auth/me")
                        .authenticated()
                        .requestMatchers("/api/auth/**").permitAll()

                        // public read-only data
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/recipes/**",
                                "/api/meal-types/**", "/api/dish-types/**", "/api/diet-types/**",
                                "/api/ingredient-categories/**", "/api/allergens/**",
                                "/api/users/*/profile", "/api/users/*/recipes",
                                "/uploads/**")
                        .permitAll()

                        // administrators only
                        .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())

                        // everything else needs a logged-in user
                        .anyRequest().authenticated())
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtService, userRepository, revokedTokenRepository),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(properties.frontendUrl()));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}