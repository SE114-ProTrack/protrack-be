    package com.protrack.protrack_be.service.impl;

    import com.protrack.protrack_be.dto.request.CompleteProfileRequest;
    import com.protrack.protrack_be.dto.request.LoginRequest;
    import com.protrack.protrack_be.dto.request.RegisterRequest;
    import com.protrack.protrack_be.dto.response.AuthResponse;
    import com.protrack.protrack_be.model.Account;
    import com.protrack.protrack_be.model.CustomUserDetails;
    import com.protrack.protrack_be.model.EmailVerificationToken;
    import com.protrack.protrack_be.model.User;
    import com.protrack.protrack_be.repository.AccountRepository;
    import com.protrack.protrack_be.repository.EmailVerificationTokenRepository;
    import com.protrack.protrack_be.repository.UserRepository;
    import com.protrack.protrack_be.service.AuthService;
    import com.protrack.protrack_be.util.JwtUtil;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.server.ResponseStatusException;

    import java.time.LocalDateTime;
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

            return new AuthResponse("", account.get().getAccId(), user.get().getName(), account.get().getEmail(), false);
        }

        @Override
        public void preRegister(RegisterRequest rq) {
            if (accountRepo.findByEmail(rq.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email đã được sử dụng");
            }

            Account acc = new Account();
            acc.setEmail(rq.getEmail());
            acc.setPassword(passwordEncoder.encode(rq.getPassword()));
            acc.setActive(false); // chưa xác minh
            accountRepo.save(acc);

            // Tạo token
            String token = UUID.randomUUID().toString();
            EmailVerificationToken tokenEntity = new EmailVerificationToken();
            tokenEntity.setToken(token);
            tokenEntity.setAccount(acc);
            tokenEntity.setExpiredAt(LocalDateTime.now().plusMinutes(15));
            tokenRepo.save(tokenEntity);

            String verifyLink = "http://frontend.com/verify?token=" + token;
            String body = "Nhấn vào link để xác minh tài khoản: " + verifyLink;

            emailService.send(acc.getEmail(), "Xác minh email ProTrack", body);
        }

        @Override
        public AuthResponse verifyEmail(String token) {
            EmailVerificationToken tokenEntity = tokenRepo.findByToken(token)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token không hợp lệ"));

            if (tokenEntity.isVerified()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token đã sử dụng");
            }

            if (tokenEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
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
            if (existing.isPresent() && existing.get().getExpiredAt().isAfter(LocalDateTime.now())) {
                token = existing.get().getToken(); // dùng lại token cũ
            } else {
                token = UUID.randomUUID().toString();
                EmailVerificationToken tokenEntity = new EmailVerificationToken(token, acc, LocalDateTime.now().plusMinutes(15));
                tokenRepo.save(tokenEntity);
            }

            String link = "http://frontend.com/verify?token=" + token;
            emailService.send(acc.getEmail(), "Xác minh lại email", "Nhấn vào đây để xác minh: " + link);
        }

    }
