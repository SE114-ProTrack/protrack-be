    package com.protrack.protrack_be.service.impl;

    import com.protrack.protrack_be.dto.request.CompleteProfileRequest;
    import com.protrack.protrack_be.dto.request.LoginRequest;
    import com.protrack.protrack_be.dto.request.RegisterRequest;
    import com.protrack.protrack_be.dto.response.AuthResponse;
    import com.protrack.protrack_be.model.*;
    import com.protrack.protrack_be.repository.AccountRepository;
    import com.protrack.protrack_be.repository.EmailVerificationTokenRepository;
    import com.protrack.protrack_be.repository.PasswordResetTokenRepository;
    import com.protrack.protrack_be.repository.UserRepository;
    import com.protrack.protrack_be.service.AuthService;
    import com.protrack.protrack_be.util.JwtUtil;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;

    import java.time.Instant;
    import java.time.LocalDateTime;
    import java.time.ZoneOffset;
    import java.time.temporal.ChronoUnit;
    import java.util.Optional;
    import java.util.UUID;

    @Service
    public class AuthServiceImpl implements AuthService {

        @Autowired
        private AccountRepository accountRepo;

        @Autowired
        private UserRepository userRepo;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private EmailVerificationTokenRepository tokenRepo;
        @Autowired
        private EmailService emailService;
        @Autowired
        private PasswordResetTokenRepository resetTokenRepo;


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
            if(account.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email không tồn tại");

            if(!passwordEncoder.matches(rq.getPassword(), account.get().getPassword())){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mật khẩu không đúng");
            }

            Optional<User> user = userRepo.findByAccount(account.get());
            if(user.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Người dùng không tồn tại");

            //tạo jwt
            String jwt = jwtUtil.generateToken(new CustomUserDetails(account.get()));

            return new AuthResponse(jwt, account.get().getAccId(), user.get().getName(), account.get().getEmail(), false);
        }

        @Override
        public void preRegister(RegisterRequest rq) {
            if (accountRepo.findByEmail(rq.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email đã được sử dụng");
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
                    <h2 style="color: #2d89ff;">Chào mừng bạn đến với ProTrack! 👋</h2>
                    <p>Nhấn vào nút bên dưới để xác minh tài khoản của bạn:</p>
                    <a href="%s" style="display: inline-block; padding: 12px 24px; margin-top: 20px; background-color: #2d89ff; color: white; text-decoration: none; border-radius: 5px;">
                        Xác minh tài khoản
                    </a>
                    <p style="margin-top: 30px; font-size: 12px; color: #888;">
                        Nếu bạn không đăng ký tài khoản, hãy bỏ qua email này.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(verifyLink);


            emailService.send(acc.getEmail(), "Xác minh email ProTrack", body);
        }

        @Override
        public AuthResponse verifyEmail(String token) {
            EmailVerificationToken tokenEntity = tokenRepo.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token không hợp lệ"));

            if (tokenEntity.isVerified()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token đã sử dụng");
            }

            if (tokenEntity.getExpiredAt().isBefore(Instant.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token đã hết hạn");
            }

            Account acc = tokenEntity.getAccount();
            acc.setActive(true);
            accountRepo.save(acc);

            tokenEntity.setVerified(true);
            tokenRepo.save(tokenEntity);

            // Tạo User sau khi xác minh
            User user = new User();
            user.setAccount(acc);
            user.setName("Tên mặc định");
            userRepo.save(user);

            String jwt = jwtUtil.generateToken(new CustomUserDetails(acc));

            return new AuthResponse(jwt, acc.getAccId(), user.getName(), acc.getEmail(), true);
        }

        @Override
        public void completeRegistration(UUID accountId, CompleteProfileRequest rq) {
            Optional<Account> accountOpt = accountRepo.findById(accountId);
            if (accountOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản không tồn tại");
            }

            Account account = accountOpt.get();
            if (!account.isActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email chưa được xác minh");
            }

            User user = userRepo.findByAccount(account).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User không tồn tại"));

            user.setName(rq.getName());
            user.setDob(rq.getDob());
            user.setAddress(rq.getAddress());
            user.setGender(rq.getGender());
            user.setPhone(rq.getPhone());
            userRepo.save(user);
        }

        @Override
        public void resendVerification(String email) {
            Account acc = accountRepo.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email chưa đăng ký"));

            if (acc.isActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản đã xác minh");
            }

            String token = UUID.randomUUID().toString();
            Optional<EmailVerificationToken> existing = tokenRepo.findByAccountAndVerifiedFalse(acc);
            if (existing.isPresent() && existing.get().getExpiredAt().isAfter(Instant.now())) {
                token = existing.get().getToken(); // dùng lại token cũ
            } else {
                token = UUID.randomUUID().toString();
                EmailVerificationToken tokenEntity = new EmailVerificationToken(token, acc, Instant.now().plus(15, ChronoUnit.MINUTES));
                tokenRepo.save(tokenEntity);
            }

            String verifyLink = "http://frontend.com/verify?token=" + token;
            String body = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 500px; margin: auto; background-color: white; border-radius: 10px; padding: 30px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #2d89ff;">Chào mừng bạn đến với ProTrack! 👋</h2>
                    <p>Nhấn vào nút bên dưới để xác minh tài khoản của bạn:</p>
                    <a href="%s" style="display: inline-block; padding: 12px 24px; margin-top: 20px; background-color: #2d89ff; color: white; text-decoration: none; border-radius: 5px;">
                        Xác minh tài khoản
                    </a>
                    <p style="margin-top: 30px; font-size: 12px; color: #888;">
                        Nếu bạn không đăng ký tài khoản, hãy bỏ qua email này.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(verifyLink);


            emailService.send(acc.getEmail(), "Xác minh lại email ProTrack", body);
        }

        @Override
        public void forgotPassword(String email) {
            Account acc = accountRepo.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email không tồn tại"));

            String token = UUID.randomUUID().toString();

            PasswordResetToken tokenEntity = new PasswordResetToken();
            tokenEntity.setToken(token);
            tokenEntity.setAccount(acc);
            tokenEntity.setExpiredAt(Instant.now().plus(15, ChronoUnit.MINUTES));
            tokenEntity.setVerified(false);
            resetTokenRepo.save(tokenEntity);

            String link = "" + "/reset-password?token=" + token;
            String body = "<p>Nhấn vào nút để đặt lại mật khẩu:</p><a href=\"" + link + "\">Đặt lại mật khẩu</a>";

            emailService.send(email, "Khôi phục mật khẩu", body);
        }

        @Override
        public void verifyResetToken(String token) {
            PasswordResetToken tokenEntity = resetTokenRepo.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token không hợp lệ"));

            if (tokenEntity.isVerified())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token đã sử dụng");

            if (tokenEntity.getExpiredAt().isBefore(Instant.now()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token đã hết hạn");

            tokenEntity.setVerified(true);
            resetTokenRepo.save(tokenEntity);
        }

        @Override
        public void resetPassword(String token, String newPassword) {
            PasswordResetToken tokenEntity = resetTokenRepo.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token không hợp lệ"));

            if (!tokenEntity.isVerified())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token chưa xác minh");

            Account acc = tokenEntity.getAccount();
            acc.setPassword(passwordEncoder.encode(newPassword));
            accountRepo.save(acc);
        }

    }
