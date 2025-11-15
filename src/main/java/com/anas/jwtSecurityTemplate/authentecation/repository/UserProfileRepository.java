package com.anas.jwtSecurityTemplate.authentecation.repository;

import com.anas.jwtSecurityTemplate.authentecation.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
}
