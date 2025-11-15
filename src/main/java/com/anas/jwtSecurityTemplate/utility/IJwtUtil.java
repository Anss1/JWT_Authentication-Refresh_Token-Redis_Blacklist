package com.anas.jwtSecurityTemplate.utility;

import com.anas.jwtSecurityTemplate.authentecation.model.User;
import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface IJwtUtil {
    String generateToken(User user);

    <T> T extractClaim(String token, Function<Claims, T> resolver);

    String extractUsername(String token);

    boolean isTokenValid(String username, String token);

}
