package com.anas.jwtSecurityTemplate.authentecation.service;

import com.anas.jwtSecurityTemplate.authentecation.dto.AuthRequest;
import com.anas.jwtSecurityTemplate.authentecation.dto.AuthResponse;
import com.anas.jwtSecurityTemplate.authentecation.dto.RegistrationRequest;
import com.anas.jwtSecurityTemplate.authentecation.model.Role;
import com.anas.jwtSecurityTemplate.authentecation.model.User;
import com.anas.jwtSecurityTemplate.authentecation.model.UserProfile;
import com.anas.jwtSecurityTemplate.authentecation.repository.UserRepository;
import com.anas.jwtSecurityTemplate.exception.InvalidRequestException;
import com.anas.jwtSecurityTemplate.exception.ResourceConflictException;
import com.anas.jwtSecurityTemplate.exception.ResourceNotFoundException;
import com.anas.jwtSecurityTemplate.token.service.IRefreshTokenService;
import com.anas.jwtSecurityTemplate.utility.IJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtUtil jwtService;
    private final IRefreshTokenService refreshTokenService;


    @Override
    public User loadUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }

    @Override
    public User loadUserByID(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Override
    public User registerUser(RegistrationRequest request){
        if(!request.password().equals(request.confirmPassword())){
            throw new InvalidRequestException("Passwords do not match.");
        }
        if(userRepository.existsByUsername(request.username())){
            throw new ResourceConflictException(request.username() + " is already exist.");
        }
        if(userRepository.existsByEmail(request.email())){
            throw new ResourceConflictException(request.email() + " is already registered.");
        }
        User newUser = User
                .builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .role(Role.USER)
                .build();

        UserProfile userProfile = new UserProfile();
        newUser.setUserProfile(userProfile);
        userProfile.setUser(newUser);

        return _saveUser(newUser);
    }

    @Override
    public AuthResponse authenticateUser(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = loadUserByUsername(request.username());
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createToken(user);
        return new AuthResponse(accessToken,refreshToken);
    }

    @Override
    public void logout(String token) {
        refreshTokenService.revokeToken(token);
    }

    @Override
    public AuthResponse refreshAccessToken(String token) {
        User user = refreshTokenService.getUserFromRefreshToken(token);
        String newRefreshToken = refreshTokenService.createToken(token,user);
        String newAccessToken = jwtService.generateToken(user);
        return new AuthResponse(newAccessToken,newRefreshToken);
    }

    private User _saveUser(User user) {
        return userRepository.save(user);
    }
}
