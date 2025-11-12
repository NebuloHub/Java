package com.nebulohub.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nebulohub.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret:my-super-secret-key}")
    private String secret;

    @Value("${api.security.token.expiration:8}")
    private Long expirationHours;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("nebulo-api")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRole())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("nebulo-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public Instant getExpirationInstant(String token) {
        if (token == null || token.isBlank()) return null;
        try {
            DecodedJWT decoded = JWT.decode(token);
            Date exp = decoded.getExpiresAt();
            return exp == null ? null : exp.toInstant();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(expirationHours).toInstant(ZoneOffset.of("-03:00"));
    }
}