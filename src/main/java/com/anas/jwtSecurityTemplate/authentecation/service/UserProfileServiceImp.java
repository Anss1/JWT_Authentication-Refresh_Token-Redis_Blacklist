package com.anas.jwtSecurityTemplate.authentecation.service;

import com.anas.jwtSecurityTemplate.authentecation.dto.UserProfileRequest;
import com.anas.jwtSecurityTemplate.authentecation.model.User;
import com.anas.jwtSecurityTemplate.authentecation.model.UserProfile;
import com.anas.jwtSecurityTemplate.authentecation.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImp implements IUserProfileService{

    private final UserProfileRepository userProfileRepository;
    private final IUserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserProfile getProfileByUsername(String username) {
        User user = userService.loadUserByUsername(username);
        return user.getUserProfile();
    }

    @Override
    @Transactional
    public UserProfile updateProfile(String username, UserProfileRequest request) {
        User user = userService.loadUserByUsername(username);
        UserProfile userProfile = user.getUserProfile();

        userProfile.setBio(request.bio());
        userProfile.setWebsite(request.website());
        return userProfileRepository.save(userProfile);
    }
}
