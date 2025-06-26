package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.ChangePasswordRequest;
import com.protrack.protrack_be.dto.request.UpdateProfileRequest;
import com.protrack.protrack_be.dto.response.UserResponse;
import com.protrack.protrack_be.model.Account;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.AccountRepository;
import com.protrack.protrack_be.repository.UserRepository;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.protrack.protrack_be.mapper.UserMapper.toResponse;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private AccountRepository accRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public Optional<User> getUserById(UUID id){
        return repo.findById(id);
    }

    @Override
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
    public UserResponse changePassword(UUID userId, ChangePasswordRequest rq){
        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        Account account = user.getAccount();

        if (!passwordEncoder.matches(rq.getOldPassword(), account.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }

        account.setPassword(passwordEncoder.encode(rq.getNewPassword()));
        accRepo.save(account);

        return toResponse(user);
    }
}
