package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Notification;
import com.protrack.protrack_be.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByReceiver(User receiver);
    Page<Notification> findByReceiverOrderByCreatedAtDesc(User receiver, Pageable pageable);
    long countByReceiverAndIsReadFalse(User receiver);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.receiver.userId = :userId AND n.isRead = false")
    int markAllAsReadForUser(@Param("userId") UUID userId);
}

