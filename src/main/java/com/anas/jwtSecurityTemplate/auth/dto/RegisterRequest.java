package com.anas.jwtSecurityTemplate.auth.dto;

import com.anas.jwtSecurityTemplate.auth.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
