package com.anas.jwtSecurityTemplate.authentecation.dto;

import com.anas.jwtSecurityTemplate.authentecation.model.UserProfile;

public record UserProfileResponse(
        String username,
        String email,
        String bio,
        String website
) {
    public static UserProfileResponse fromEntity(UserProfile userProfile){
        if (userProfile == null)
            return null;
        return new UserProfileResponse(
                userProfile.getUser().getUsername(),
                userProfile.getUser().getEmail(),
                userProfile.getBio(),
                userProfile.getWebsite()
        );
    }
}
