package com.anas.jwtSecurityTemplate.token.service;

import com.anas.jwtSecurityTemplate.authentecation.model.User;
import com.anas.jwtSecurityTemplate.token.entity.RefreshToken;
import org.springframework.transaction.annotation.Transactional;

public interface IRefreshTokenService {
    @Transactional
    String createToken(User user);

    @Transactional
    String createToken(String oldToken, User user);

    RefreshToken verifyAndRetrieveToken(String token);

    @Transactional
    void revokeToken(String token);

    @Transactional
    void deleteByUser(User user);

    User getUserFromRefreshToken(String refreshToken);

    void cleanOldRevokedTokens();
}
