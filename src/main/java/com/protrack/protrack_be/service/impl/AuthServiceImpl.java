package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.LoginRequest;
import com.protrack.protrack_be.dto.request.RegisterRequest;
import com.protrack.protrack_be.dto.response.AuthResponse;
import com.protrack.protrack_be.model.Account;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.AccountRepository;
import com.protrack.protrack_be.repository.UserRepository;
import com.protrack.protrack_be.service.AuthService;
import com.protrack.protrack_be.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private AccountRepository accountRepo;
    private UserRepository userRepo;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest rq){
        if (accountRepo.findByEmail(rq.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        Account account = new Account();
        account.setEmail(rq.getEmail());
        account.setPassword(passwordEncoder.encode(rq.getPassword()));
        accountRepo.save(account);

        User user = new User();
        user.setName(rq.getName());
        user.setDob(rq.getDob());
        user.setAddress(rq.getAddress());
        user.setGender(rq.getGender());
        user.setPhone(rq.getPhone());
        user.setAccount(account);
        userRepo.save(user);

        //tạo jwt

        return new AuthResponse("", account.getAccId(), user.getName(), account.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest rq){
        Optional<Account> account = accountRepo.findByEmail(rq.getEmail());
        if(account.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email không tồn tại");

        if(!passwordEncoder.matches(rq.getPassword(), account.get().getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mật khẩu không đúng");
        }

        Optional<User> user = userRepo.findByAccount(account.get());
        if(user.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Người dùng không tồn tại");

        //tạo jwt

        return new AuthResponse("", account.get().getAccId(), user.get().getName(), account.get().getEmail());
    }
}
