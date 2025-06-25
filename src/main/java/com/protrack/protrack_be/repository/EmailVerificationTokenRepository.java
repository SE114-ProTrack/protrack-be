package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Account;
import com.protrack.protrack_be.model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findByAccountAndVerifiedFalse(Account account);
}

