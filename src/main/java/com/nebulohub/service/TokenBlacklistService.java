package com.nebulohub.service;

import com.nebulohub.domain.token.TokenBlacklist;
import com.nebulohub.domain.token.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final TokenBlacklistRepository repository;

    @Transactional
    public void blacklistToken(String token, Instant expiresAt) {
        if (token == null || token.isBlank()) {
            return;
        }
        if (repository.existsByToken(token)) {
            return;
        }
        TokenBlacklist entry = new TokenBlacklist(token, expiresAt);
        repository.save(entry);
    }

    public boolean isBlacklisted(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        return repository.existsByToken(token);
    }
}