package com.anas.jwtSecurityTemplate.authentecation.controller;

import com.anas.jwtSecurityTemplate.authentecation.dto.UserProfileRequest;
import com.anas.jwtSecurityTemplate.authentecation.dto.UserProfileResponse;
import com.anas.jwtSecurityTemplate.authentecation.model.UserProfile;
import com.anas.jwtSecurityTemplate.authentecation.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final IUserProfileService userProfileService;

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable String username) {
        UserProfile userProfile = userProfileService.getProfileByUsername(username);
        UserProfileResponse response = UserProfileResponse.fromEntity(userProfile);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    @PreAuthorize("authentication.principal.username == #username")
    public ResponseEntity<UserProfileResponse> updateProfile(@PathVariable String username,
                                                             @RequestBody UserProfileRequest request) {
        UserProfile userProfile = userProfileService.updateProfile(username,request);
        UserProfileResponse response = UserProfileResponse.fromEntity(userProfile);
        return ResponseEntity.ok(response);
    }
}
