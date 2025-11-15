package com.anas.jwtSecurityTemplate.authentecation.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
){}
