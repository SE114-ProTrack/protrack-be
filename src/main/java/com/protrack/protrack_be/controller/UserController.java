package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ChangePasswordRequest;
import com.protrack.protrack_be.dto.request.UpdateProfileRequest;
import com.protrack.protrack_be.dto.response.ProfileResponse;
import com.protrack.protrack_be.dto.response.UserResponse;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.service.UserService;
import com.protrack.protrack_be.service.impl.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "API quản lý người dùng")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping(
            value = "/{userId}/uploadAvatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Thay đổi avatar người dùng")
    public ResponseEntity<String> uploadUserAvatar(
            @PathVariable UUID userId,
            @RequestPart("file") MultipartFile file) {
        String avatarUrl = fileStorageService.store(file);
        userService.updateUserAvatar(userId, avatarUrl);
        return ResponseEntity.ok(avatarUrl);
    }

    @Operation(summary = "Cập nhật thông tin người dùng")
    @PutMapping("/{userId}/updateProfile")
    public ResponseEntity<?> updateUserProfile(@PathVariable UUID userId, @RequestBody UpdateProfileRequest request) {
        // Cập nhật thông tin người dùng
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @Operation(summary = "Đổi mật khẩu người dùng")
    @PostMapping("/{userId}/changePassword")
    public ResponseEntity<?> changePassword(@PathVariable UUID userId, @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(userId, request));
    }

    @GetMapping("/{userId}/status")
    public ResponseEntity<String> getUserStatus(@PathVariable String userId) {
        Boolean isOnline = redisTemplate.hasKey("user:online:" + userId);
        return ResponseEntity.ok(isOnline ? "online" : "offline");
    }

    @Operation(summary = "Người cùng dự án nhưng chưa từng trò chuyện")
    @GetMapping("/same-project-users-not-chatted")
    public ResponseEntity<List<UserResponse>> getUsersSameProjectButNoChat() {
        List<UserResponse> responses = userService.getUsersSharingProjects();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }
}
