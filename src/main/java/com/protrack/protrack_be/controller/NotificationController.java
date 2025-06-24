package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.response.NotificationResponse;
import com.protrack.protrack_be.model.Notification;
import com.protrack.protrack_be.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    @Autowired
    private NotificationService service;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(@RequestBody UUID userId) {
        List<NotificationResponse> responses = service.getAll(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody @Valid NotificationRequest request) {
        NotificationResponse response = service.create(request);
        return  ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable UUID id) {
        service.markAsRead(id);
        return ResponseEntity.ok("Marked notification " + id + " read");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted notification successfully");
    }
}
