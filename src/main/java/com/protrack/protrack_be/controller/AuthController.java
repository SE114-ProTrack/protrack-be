package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.LoginRequest;
import com.protrack.protrack_be.dto.request.RegisterRequest;
import com.protrack.protrack_be.dto.response.AuthResponse;
import com.protrack.protrack_be.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest rq){
        AuthResponse response = authService.register(rq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest rq){
        AuthResponse response = authService.login(rq);
        return ResponseEntity.ok(response);
    }
}
