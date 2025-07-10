package com.anas.jwtSecurityTemplate.auth.service;

import com.anas.jwtSecurityTemplate.auth.dto.*;
import com.anas.jwtSecurityTemplate.auth.entity.User;
import com.anas.jwtSecurityTemplate.auth.jwt.JwtService;
import com.anas.jwtSecurityTemplate.auth.repository.UserRepository;
import com.anas.jwtSecurityTemplate.token.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        User user = refreshTokenService.validateRefreshToken(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(user);

        return new AuthResponse(newAccessToken, refreshToken); // keep same refresh token
    }

    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String accessToken = authHeader.substring(7);
        long remainingMillis = jwtService.getRemainingMillis(accessToken);

        User user = userRepository.findByEmail(jwtService.extractUsername(accessToken)).orElseThrow();

        refreshTokenService.revokeRefreshToken(user); //
        tokenBlacklistService.blacklistToken(accessToken, remainingMillis);
    }
}
