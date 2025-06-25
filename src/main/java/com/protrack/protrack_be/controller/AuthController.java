package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.*;
import com.protrack.protrack_be.dto.response.AuthResponse;
import com.protrack.protrack_be.service.AuthService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API xác thực người dùng")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "Đăng ký tài khoản", description = "Gửi email xác minh đến người dùng")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest rq){
        authService.preRegister(rq);
        return ResponseEntity.ok("Email has been sent for verification");
    }

    @Operation(summary = "Đăng nhập", description = "Đăng nhập bằng email và mật khẩu")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest rq){
        AuthResponse response = authService.login(rq);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xác minh email", description = "Xác minh tài khoản bằng token trong email")
    @GetMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token)); // Xác minh token và trả về thông tin người dùng
    }

    @Operation(summary = "Hoàn tất hồ sơ người dùng", description = "Sau khi xác minh email thành công")
    @PostMapping("/complete-profile")
    public ResponseEntity<?> completeProfile(@RequestBody @Valid CompleteProfileRequest rq) {
        authService.completeRegistration(rq.getAccountId(), rq);
        return ResponseEntity.ok("Hoàn tất đăng ký thành công");
    }

    @Operation(summary = "Gửi lại email xác minh", description = "Dành cho tài khoản chưa xác minh email")
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam String email) {
        authService.resendVerification(email);
        return ResponseEntity.ok("Đã gửi lại email xác minh");
    }

    @Operation(summary = "Gửi mail khôi phục mật khẩu")
    @PostMapping("/forgot-password/request")
    public ResponseEntity<?> requestReset(@RequestBody @Valid ForgotPasswordRequest rq) {
        authService.forgotPassword(rq.getEmail());
        return ResponseEntity.ok("Đã gửi link khôi phục mật khẩu");
    }

    @Operation(summary = "Xác minh token khôi phục mật khẩu")
    @PostMapping("/forgot-password/verify")
    public ResponseEntity<?> verifyResetToken(@RequestBody @Valid VerifyResetTokenRequest rq) {
        authService.verifyResetToken(rq.getToken());
        return ResponseEntity.ok("Token hợp lệ");
    }

    @Operation(summary = "Đặt lại mật khẩu mới")
    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest rq) {
        authService.resetPassword(rq.getToken(), rq.getNewPassword());
        return ResponseEntity.ok("Đặt lại mật khẩu thành công");
    }
}
