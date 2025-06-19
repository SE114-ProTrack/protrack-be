package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {}

