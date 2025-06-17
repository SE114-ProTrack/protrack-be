package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Account;
import com.protrack.protrack_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByAccount(Account account);
    Optional<User> findById(UUID userId);
}
