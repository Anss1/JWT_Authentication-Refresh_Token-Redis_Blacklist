package com.anas.jwtSecurityTemplate.authentecation.service;

import com.anas.jwtSecurityTemplate.authentecation.dto.AuthRequest;
import com.anas.jwtSecurityTemplate.authentecation.dto.AuthResponse;
import com.anas.jwtSecurityTemplate.authentecation.dto.RegistrationRequest;
import com.anas.jwtSecurityTemplate.authentecation.model.User;

public interface IUserService {
    User loadUserByUsername(String username);

    User loadUserByID(Long userId);

    User registerUser(RegistrationRequest request);

    AuthResponse authenticateUser(AuthRequest request);

    void logout(String refreshToken);

    AuthResponse refreshAccessToken(String refreshToken);
}
