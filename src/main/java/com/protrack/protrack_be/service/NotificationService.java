package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationService {
    Page<NotificationResponse> getAll(Pageable pageable);
    Optional<NotificationResponse> getById(UUID id);
    NotificationResponse create(NotificationRequest request);
    NotificationResponse  markAsRead(UUID id);
    void delete(UUID id);
    long countUnread();
    int markAllAsRead();
}
