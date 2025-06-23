package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.response.NotificationResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationService {
    List<NotificationResponse> getAll(UUID userId);
    Optional<NotificationResponse> getById(UUID id);
    NotificationResponse create(NotificationRequest request);
    void markAsRead(UUID id);
    void delete(UUID id);
}
