package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.UserResponse;
import com.protrack.protrack_be.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user){
        if (user == null || user.getAccount() == null) return null;

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getAccount().getEmail(),
                user.getDob(),
                user.getGender(),
                user.getPhone(),
                user.getAddress(),
                user.getAvatarUrl()
        );
    }
}
