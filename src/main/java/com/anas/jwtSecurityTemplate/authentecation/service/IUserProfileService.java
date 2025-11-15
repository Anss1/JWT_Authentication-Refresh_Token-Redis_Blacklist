package com.anas.jwtSecurityTemplate.authentecation.service;

import com.anas.jwtSecurityTemplate.authentecation.dto.UserProfileRequest;
import com.anas.jwtSecurityTemplate.authentecation.model.UserProfile;

public interface IUserProfileService {
    UserProfile getProfileByUsername(String username);

    UserProfile updateProfile(String username, UserProfileRequest request);
}
