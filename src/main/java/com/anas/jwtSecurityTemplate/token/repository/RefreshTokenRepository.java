package com.anas.jwtSecurityTemplate.token.repository;

import com.anas.jwtSecurityTemplate.auth.entity.User;
import com.anas.jwtSecurityTemplate.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByUser(User user);
    void deleteAllByRevokedIsTrueAndExpiryDateBefore(Instant threshold);
    RefreshToken[] findAllByUser(User user);
}
