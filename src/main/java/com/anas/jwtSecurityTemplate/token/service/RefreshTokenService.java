package com.anas.jwtSecurityTemplate.token.service;

import com.anas.jwtSecurityTemplate.auth.entity.User;
import com.anas.jwtSecurityTemplate.token.entity.RefreshToken;
import com.anas.jwtSecurityTemplate.token.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final static long EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days

    public String createRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(EXPIRATION))
                .revoked(false)
                .build();

        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public User validateRefreshToken(String tokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return token.getUser();
    }

    @Transactional
    public void revokeRefreshToken(String tokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
    @Transactional
    public void revokeRefreshToken(User user) {
        for (RefreshToken token : refreshTokenRepository.findAllByUser(user)) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }

    @Transactional
    public void deleteTokensByUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    @Scheduled(cron = "0 0 3 * * *") // Every day at 3 AM
    @Transactional
    public void cleanOldRevokedTokens() {
        refreshTokenRepository.deleteAllByRevokedIsTrueAndExpiryDateBefore(Instant.now().minus(30, ChronoUnit.DAYS));
    }
}
