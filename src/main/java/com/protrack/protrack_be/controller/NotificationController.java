package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.response.NotificationResponse;
import com.protrack.protrack_be.model.Notification;
import com.protrack.protrack_be.service.NotificationService;
import com.protrack.protrack_be.validation.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "API thông báo")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NotificationResponse> responses = service.getAll(PageRequest.of(page, size));
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy thông báo theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo thông báo")
    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody @Validated(CreateGroup.class) NotificationRequest request) {
        NotificationResponse response = service.create(request);
        return  ResponseEntity.ok(response);
    }

    @Operation(summary = "Đánh dấu đã đọc")
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable UUID id) {
        NotificationResponse response = service.markAsRead(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Đánh dấu tất cả thông báo là đã đọc")
    @PutMapping("/mark-all-read")
    public ResponseEntity<?> markAllAsRead() {
        int updated = service.markAllAsRead();
        return ResponseEntity.ok("Marked " + updated + " notifications as read");
    }

    @Operation(summary = "Xóa thông báo")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted notification successfully");
    }

    @Operation(summary = "Số lượng thông báo chưa đọc")
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        long count = service.countUnread();
        return ResponseEntity.ok(count);
    }
}
