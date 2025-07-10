package com.anas.jwtSecurityTemplate.auth.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}
