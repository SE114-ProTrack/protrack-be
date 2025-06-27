package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    Optional<Invitation> findByInvitationToken(String token);
    boolean existsByInvitationEmailAndAccepted(String email, boolean accepted);
}
