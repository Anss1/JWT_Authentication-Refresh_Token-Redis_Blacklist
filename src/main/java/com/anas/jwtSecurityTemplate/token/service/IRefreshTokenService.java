package com.anas.jwtSecurityTemplate.token.service;

import com.anas.jwtSecurityTemplate.auth.entity.User;

public interface IRefreshTokenService {
    String createRefreshToken(User user);
    User validateRefreshToken(String tokenStr);
    void revokeRefreshToken(String tokenStr);
    String rotateRefreshToken(String oldTokenStr);
    void revokeRefreshToken(User user);
    void deleteTokensByUser(User user);
    void cleanOldRevokedTokens();
}
