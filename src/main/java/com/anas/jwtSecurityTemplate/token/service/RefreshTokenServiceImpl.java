package com.anas.jwtSecurityTemplate.token.service;

import com.anas.jwtSecurityTemplate.auth.entity.User;
import com.anas.jwtSecurityTemplate.exception.InvalidTokenException;
import com.anas.jwtSecurityTemplate.token.entity.RefreshToken;
import com.anas.jwtSecurityTemplate.token.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token.expiration-ms}")
    private long refreshTokenExpirationMs;

    public String createRefreshToken(User user) {
        String secret = UUID.randomUUID().toString();
        String identifier = UUID.randomUUID().toString();
        String hashedSecret = passwordEncoder.encode(secret);

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .tokenIdentifier(identifier)
                .token(hashedSecret)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();

        refreshTokenRepository.save(token);
        return identifier + ":" + secret;
    }

    public User validateRefreshToken(String tokenStr) {
        String[] tokenParts = parseToken(tokenStr);
        String identifier = tokenParts[0];
        String secret = tokenParts[1];

        RefreshToken token = refreshTokenRepository.findByTokenIdentifier(identifier)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token: identifier not found"));

        if (!passwordEncoder.matches(secret, token.getToken())) {
            throw new RuntimeException("Invalid refresh token: secret mismatch");
        }

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token has expired");
        }

        return token.getUser();
    }

    @Transactional
    public void revokeRefreshToken(String tokenStr) {
        String[] tokenParts = parseToken(tokenStr);
        String identifier = tokenParts[0];
        RefreshToken token = refreshTokenRepository.findByTokenIdentifier(identifier)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    @Transactional
    public String rotateRefreshToken(String oldTokenStr) {
        User user = validateRefreshToken(oldTokenStr);
        revokeRefreshToken(oldTokenStr);
        return createRefreshToken(user);
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

    private String[] parseToken(String tokenStr) {
        if (tokenStr == null || !tokenStr.contains(":")) {
            throw new IllegalArgumentException("Invalid refresh token format");
        }
        return tokenStr.split(":", 2);
    }
}