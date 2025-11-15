package com.anas.jwtSecurityTemplate.token.repository;

import com.anas.jwtSecurityTemplate.authentecation.model.User;
import com.anas.jwtSecurityTemplate.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByTokenIdentifier(String tokenIdentifier);
    void deleteAllByUser(User user);
    void deleteAllByRevokedIsTrueAndExpiryDateBefore(Instant threshold);
    List<RefreshToken> findAllByUser(User user);
}
