    package com.protrack.protrack_be.service.impl;

    import com.protrack.protrack_be.dto.request.CompleteProfileRequest;
    import com.protrack.protrack_be.dto.request.LoginRequest;
    import com.protrack.protrack_be.dto.request.RegisterRequest;
    import com.protrack.protrack_be.dto.response.AuthResponse;
    import com.protrack.protrack_be.dto.response.CompleteRegistrationResponse;
    import com.protrack.protrack_be.model.*;
    import com.protrack.protrack_be.repository.*;
    import com.protrack.protrack_be.service.AuthService;
    import com.protrack.protrack_be.service.UserService;
    import com.protrack.protrack_be.util.JwtUtil;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;

    import java.time.Instant;
    import java.time.temporal.ChronoUnit;
    import java.util.Optional;
    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class AuthServiceImpl implements AuthService {

        @Autowired
        private final AccountRepository accountRepo;

        @Autowired
        private final UserRepository userRepo;

        @Autowired
        private final JwtUtil jwtUtil;

        @Autowired
        private final PasswordEncoder passwordEncoder;

        @Autowired
        private final EmailVerificationTokenRepository tokenRepo;
        @Autowired
        private final EmailService emailService;
        @Autowired
        private final PasswordResetTokenRepository resetTokenRepo;

        @Autowired
        private final InvitationRepository invitationRepo;

        @Autowired
        private final UserService userService;


//        @Override
//        public AuthResponse register(RegisterRequest rq){
//            if (accountRepo.findByEmail(rq.getEmail()).isPresent()) {
//                throw new IllegalArgumentException("Email đã được sử dụng");
//            }
//
//            Account account = new Account();
//            account.setEmail(rq.getEmail());
//            account.setPassword(passwordEncoder.encode(rq.getPassword()));
//            accountRepo.save(account);
//
//            User user = new User();
//            user.setName(rq.getName());
//            user.setDob(rq.getDob());
//            user.setAddress(rq.getAddress());
//            user.setGender(rq.getGender());
//            user.setPhone(rq.getPhone());
//            user.setAccount(account);
//            userRepo.save(user);
//
//            return new AuthResponse("", account.getAccId(), user.getName(), account.getEmail(), false);
//        }

        @Override
        public AuthResponse login(LoginRequest rq){
            Optional<Account> account = accountRepo.findByEmail(rq.getEmail());
            if(account.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email not found");

            if(!passwordEncoder.matches(rq.getPassword(), account.get().getPassword())){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password incorrect");
            }

            Optional<User> user = userRepo.findByAccount(account.get());
            if(user.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");

            //tạo jwt
            String jwt = jwtUtil.generateToken(new CustomUserDetails(account.get()));

            return new AuthResponse(jwt, account.get().getAccId(), user.get().getName(), account.get().getEmail(), false);
        }

        @Override
        public void preRegister(RegisterRequest rq) {
            if (accountRepo.findByEmail(rq.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email has been used");
            }

            Account acc = new Account();
            acc.setEmail(rq.getEmail());
            acc.setPassword(passwordEncoder.encode(rq.getPassword()));
            acc.setActive(false);
            accountRepo.save(acc);

            // Tạo token
            String token = UUID.randomUUID().toString();
            EmailVerificationToken tokenEntity = new EmailVerificationToken();
            tokenEntity.setToken(token);
            tokenEntity.setAccount(acc);
            tokenEntity.setExpiredAt(Instant.now().plus(15, ChronoUnit.MINUTES));
            tokenRepo.save(tokenEntity);

            String verifyLink = "http://frontend.com/verify?token=" + token;
            String body = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 500px; margin: auto; background-color: white; border-radius: 10px; padding: 30px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #2d89ff;">Welcome to ProTrack! 👋</h2>
                    <p>Tap on the following button to verify your account:</p>
                    <a href="%s" style="display: inline-block; padding: 12px 24px; margin-top: 20px; background-color: #2d89ff; color: white; text-decoration: none; border-radius: 5px;">
                        Verify here!
                    </a>
                    <p style="margin-top: 30px; font-size: 12px; color: #888;">
                        If you did not register an account for ProTrack, please ignore this email.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(verifyLink);


            emailService.send(acc.getEmail(), "Account Verifying for ProTrack", body);
        }

        @Override
        public AuthResponse verifyEmail(String token, String email) {
            EmailVerificationToken tokenEntity = tokenRepo.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalid"));
            if (tokenEntity.isVerified()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has been used");
            }

            if (tokenEntity.getExpiredAt().isBefore(Instant.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired");
            }

            if (!tokenEntity.getAccount().getEmail().equalsIgnoreCase(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token/email mismatch");
            }


            Account acc = tokenEntity.getAccount();
            acc.setActive(true);
            accountRepo.save(acc);

            tokenEntity.setVerified(true);
            tokenRepo.save(tokenEntity);

            // Tạo User sau khi xác minh
            User user = new User();
            user.setAccount(acc);
            user.setName("Default");
            userRepo.save(user);

            String jwt = jwtUtil.generateToken(new CustomUserDetails(acc));

            return new AuthResponse(jwt, acc.getAccId(), user.getName(), acc.getEmail(), true);
        }

        @Override
        public CompleteRegistrationResponse completeRegistration(UUID accountId, CompleteProfileRequest rq) {
            Optional<Account> accountOpt = accountRepo.findById(accountId);
            if (accountOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
            }

            Account account = accountOpt.get();
            if (!account.isActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email has not been verified");
            }

            User user = userRepo.findByAccount(account).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

            user.setName(rq.getName());
            user.setDob(rq.getDob());
            user.setAddress(rq.getAddress());
            user.setGender(rq.getGender());
            user.setPhone(rq.getPhone());
            userRepo.save(user);

            boolean hasPendingInvitation = invitationRepo.existsByInvitationEmailAndAccepted(user.getAccount().getEmail(), false);

            return new CompleteRegistrationResponse(
                    "Registered successfully",
                    hasPendingInvitation
            );
        }

        @Override
        public void resendVerification(String email) {
            Account acc = accountRepo.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email has not registered"));

            if (acc.isActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account has been verified");
            }

            String token = UUID.randomUUID().toString();
            Optional<EmailVerificationToken> existing = tokenRepo.findByAccountAndVerifiedFalse(acc);
            if (existing.isPresent() && existing.get().getExpiredAt().isAfter(Instant.now())) {
                token = existing.get().getToken(); // dùng lại token cũ
            } else {
                EmailVerificationToken tokenEntity = new EmailVerificationToken(token, acc, Instant.now().plus(15, ChronoUnit.MINUTES));
                tokenRepo.save(tokenEntity);
            }

            String verifyLink = "http://frontend.com/verify?token=" + token;
            String body = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 500px; margin: auto; background-color: white; border-radius: 10px; padding: 30px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #2d89ff;">Welcome to ProTrack! 👋</h2>
                    <p>Tap on the following button to verify your account:</p>
                    <a href="%s" style="display: inline-block; padding: 12px 24px; margin-top: 20px; background-color: #2d89ff; color: white; text-decoration: none; border-radius: 5px;">
                        Verify here!
                    </a>
                    <p style="margin-top: 30px; font-size: 12px; color: #888;">
                        If you did not register an account for ProTrack, please ignore this email.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(verifyLink);


            emailService.send(acc.getEmail(), "[RESENT] Account Verifying for ProTrack", body);
        }

        @Override
        public void forgotPassword(String email) {
            Account acc = accountRepo.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

            String token = UUID.randomUUID().toString();

            PasswordResetToken tokenEntity = new PasswordResetToken();
            tokenEntity.setToken(token);
            tokenEntity.setAccount(acc);
            tokenEntity.setExpiredAt(Instant.now().plus(15, ChronoUnit.MINUTES));
            tokenEntity.setVerified(false);
            resetTokenRepo.save(tokenEntity);

            String link = "..." + "/reset-password?token=" + token;
            String body = "<p>Tap here to reset password: </p><a href=\"" + link + "\">Reset password</a>";

            emailService.send(email, "Reset password", body);
        }

        @Override
        public void verifyResetToken(String token, String email) {
            PasswordResetToken tokenEntity = resetTokenRepo.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalid"));

            if (tokenEntity.isVerified())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has been used");

            if (tokenEntity.getExpiredAt().isBefore(Instant.now()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired");

            if (!tokenEntity.getAccount().getEmail().equalsIgnoreCase(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token/email mismatch");
            }

            tokenEntity.setVerified(true);
            resetTokenRepo.save(tokenEntity);
        }

        @Override
        public void resetPassword(String token, String email, String newPassword) {
            PasswordResetToken tokenEntity = resetTokenRepo.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalid"));

            if (!tokenEntity.isVerified())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has not been verified");

            if (!tokenEntity.getAccount().getEmail().equalsIgnoreCase(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token/email mismatch");
            }

            Account acc = tokenEntity.getAccount();
            acc.setPassword(passwordEncoder.encode(newPassword));
            accountRepo.save(acc);
        }

    }
