package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {}
