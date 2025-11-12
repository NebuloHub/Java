package com.nebulohub.service;

import com.nebulohub.domain.token.TokenBlacklist;
import com.nebulohub.domain.token.TokenBlacklistRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenBlacklistService {

    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);

    private final TokenBlacklistRepository repository;

    public TokenBlacklistService(TokenBlacklistRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void blacklistToken(String token, Instant expiresAt) {
        if (token == null || token.isBlank()) {
            logger.warn("Attempted to blacklist empty token — skipping");
            return;
        }

        try {
            if (repository.existsByToken(token)) {
                logger.info("Token already blacklisted (skipping): {}", token.substring(0, Math.min(token.length(), 32)));
                return;
            }

            TokenBlacklist entry = new TokenBlacklist(token, expiresAt == null ? Instant.now().plusSeconds(3600) : expiresAt);
            repository.saveAndFlush(entry);
            logger.info("Blacklisted token (truncated) {} exp={}", token.substring(0, Math.min(token.length(), 32)), entry.getExpiresAt());
        } catch (Exception e) {
            logger.error("Failed to persist token blacklist entry", e);
        }
    }

    public boolean isBlacklisted(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            return repository.existsByToken(token);
        } catch (Exception e) {
            logger.error("Error checking blacklist for token", e);
            return false;
        }
    }

}
