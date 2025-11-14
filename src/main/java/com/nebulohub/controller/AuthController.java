package com.nebulohub.controller;

import com.nebulohub.infra.security.LoginDto;
import com.nebulohub.infra.security.TokenResponse;
import com.nebulohub.service.TokenBlacklistService;
import com.nebulohub.service.TokenService;
import com.nebulohub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and Logout endpoints")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final TokenBlacklistService tokenBlacklistService;

    @Operation(summary = "Login with email and password")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginDto dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @Operation(summary = "Logout and invalidate the current token")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = recoverToken(request);
        if (token != null) {
            Instant expiresAt = tokenService.getExpirationInstant(token);
            if (expiresAt == null) {
                expiresAt = Instant.now().plusSeconds(3600);
            }
            tokenBlacklistService.blacklistToken(token, expiresAt);
        }
        return ResponseEntity.noContent().build();
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}