package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.LoginRequest;
import com.protrack.protrack_be.dto.request.RegisterRequest;
import com.protrack.protrack_be.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest rq);
    AuthResponse login(LoginRequest rq);
}
