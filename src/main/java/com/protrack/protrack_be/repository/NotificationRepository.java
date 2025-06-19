package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {}

