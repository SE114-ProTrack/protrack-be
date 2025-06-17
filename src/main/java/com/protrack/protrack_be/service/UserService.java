package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.ChangePasswordRequest;
import com.protrack.protrack_be.dto.request.UpdateProfileRequest;
import com.protrack.protrack_be.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse getCurrentUser(UUID userId);
    UserResponse updateProfile(UUID userId, UpdateProfileRequest rq);
    UserResponse changePassword(UUID userId, ChangePasswordRequest rq);
}
