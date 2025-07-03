package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.CompleteProfileRequest;
import com.protrack.protrack_be.dto.request.LoginRequest;
import com.protrack.protrack_be.dto.request.RegisterRequest;
import com.protrack.protrack_be.dto.response.AuthResponse;
import com.protrack.protrack_be.dto.response.CompleteRegistrationResponse;
import com.protrack.protrack_be.model.User;

import java.util.UUID;

public interface AuthService {
//    AuthResponse register(RegisterRequest rq);
    AuthResponse login(LoginRequest rq);
    void preRegister(RegisterRequest rq);
    AuthResponse verifyEmail(String token, String email);
    CompleteRegistrationResponse completeRegistration(UUID accountId, CompleteProfileRequest rq);
    void resendVerification(String email);

    void forgotPassword(String email);
    void verifyResetToken(String token, String email);
    void resetPassword(String token, String email, String newPassword);

}
