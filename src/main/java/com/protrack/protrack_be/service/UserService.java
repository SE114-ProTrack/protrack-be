package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.ChangePasswordRequest;
import com.protrack.protrack_be.dto.request.UpdateProfileRequest;
import com.protrack.protrack_be.dto.response.UserResponse;
import com.protrack.protrack_be.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User getCurrentUser();
    Optional<User> getUserById(UUID id);
    UserResponse updateProfile(UUID userId, UpdateProfileRequest rq);
    UserResponse changePassword(UUID userId, ChangePasswordRequest rq);
}
