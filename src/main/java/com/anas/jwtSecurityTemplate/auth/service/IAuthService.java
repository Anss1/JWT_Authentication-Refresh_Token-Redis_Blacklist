package com.anas.jwtSecurityTemplate.auth.service;

import com.anas.jwtSecurityTemplate.auth.dto.AuthRequest;
import com.anas.jwtSecurityTemplate.auth.dto.AuthResponse;
import com.anas.jwtSecurityTemplate.auth.dto.RegisterRequest;
import org.springframework.stereotype.Service;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String authHeader);
}
