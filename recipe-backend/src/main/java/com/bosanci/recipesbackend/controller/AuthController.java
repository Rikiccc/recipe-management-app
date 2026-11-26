package com.bosanci.recipesbackend.controller;

import com.bosanci.recipesbackend.dto.MessageResponse;
import com.bosanci.recipesbackend.dto.UserDto;
import com.bosanci.recipesbackend.dto.auth.*;
import com.bosanci.recipesbackend.security.AuthenticatedUser;
import com.bosanci.recipesbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/activate")
    public MessageResponse activate(@Valid @RequestBody TokenRequest request) {
        return authService.activate(request.token());
    }

    @PostMapping("/resend-activation")
    public MessageResponse resendActivation(@Valid @RequestBody EmailRequest request) {
        return authService.resendActivation(request.email());
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@AuthenticationPrincipal AuthenticatedUser user) {
        authService.logout(user);
    }

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal AuthenticatedUser user) {
        return authService.me(user.id());
    }
}