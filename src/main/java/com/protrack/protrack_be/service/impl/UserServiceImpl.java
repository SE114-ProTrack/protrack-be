package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.ChangePasswordRequest;
import com.protrack.protrack_be.dto.request.UpdateProfileRequest;
import com.protrack.protrack_be.dto.response.ProfileResponse;
import com.protrack.protrack_be.dto.response.UserResponse;
import com.protrack.protrack_be.mapper.UserMapper;
import com.protrack.protrack_be.model.Account;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.*;
import com.protrack.protrack_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.protrack.protrack_be.mapper.UserMapper.toResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repo;

    @Autowired
    private final AccountRepository accRepo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final ProjectMemberRepository projectMemberRepository;

    @Autowired
    private final TaskMemberRepository taskMemberRepository;

    @Autowired
    private final MessageRepository messageRepository;

    @Override
    public User getCurrentUser(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Không có người dùng đang đăng nhập");
        }

        String email = auth.getName(); // hoặc ((CustomUserDetails) auth.getPrincipal()).getEmail()

        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + email));
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<User> getUserById(UUID id){
        return repo.findById(id);
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<User> getUserByEmail(String email){
        return repo.findByEmail(email);
    }

    @Override
    @EnableSoftDeleteFilter
    public UserResponse updateProfile(UUID userId, UpdateProfileRequest rq){
        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        user.setName(rq.getName());
        user.setDob(rq.getDob());
        user.setGender(rq.getGender());
        user.setPhone(rq.getPhone());
        user.setAddress(rq.getAddress());
        user.setAvatarUrl(rq.getAvatarUrl());

        repo.save(user);
        return toResponse(user);
    }

    @Override
    @EnableSoftDeleteFilter
    public UserResponse changePassword(UUID userId, ChangePasswordRequest rq){
        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Account account = user.getAccount();

        if (!passwordEncoder.matches(rq.getOldPassword(), account.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        account.setPassword(passwordEncoder.encode(rq.getNewPassword()));
        accRepo.save(account);

        return toResponse(user);
    }

    @Override
    @EnableSoftDeleteFilter
    public void updateUserAvatar(UUID userId, String avatarUrl){
        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setAvatarUrl(avatarUrl);
        repo.save(user);
    }

    @Override
    @EnableSoftDeleteFilter
    public List<UserResponse> getUsersSharingProjects() {
        UUID currentUserId = getCurrentUser().getUserId();
        List<User> users = repo.findUsersInSameProjectWithoutConversation(currentUserId);
        return users.stream().map(UserMapper::toResponse).toList();
    }

    @Override
    public ProfileResponse getUserProfile() {
        User user = getCurrentUser();
        UUID userId = user.getUserId();

        int projectCount = projectMemberRepository.countProjectsByUser(userId);
        int taskCount = taskMemberRepository.countTasksByUser(userId);
        int contactCount = messageRepository.countContactUsersByUserId(userId);

        return new ProfileResponse(
                user.getUserId(),
                user.getName(),
                user.getAccount().getEmail(),
                user.getAvatarUrl(),
                projectCount,
                taskCount,
                contactCount
        );
    }

}
