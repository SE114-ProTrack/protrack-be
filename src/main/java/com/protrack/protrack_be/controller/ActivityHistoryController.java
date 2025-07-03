package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ActivityHistoryRequest;
import com.protrack.protrack_be.dto.response.ActivityHistoryResponse;
import com.protrack.protrack_be.dto.response.CommentResponse;
import com.protrack.protrack_be.model.ActivityHistory;
import com.protrack.protrack_be.service.ActivityHistoryService;
import com.protrack.protrack_be.validation.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity-history")
@RequiredArgsConstructor
@Tag(name = "Activity History", description = "API lịch sử hoạt động")
public class ActivityHistoryController {

    @Autowired
    ActivityHistoryService service;

    @Operation(summary = "Lấy toàn bộ lịch sử hoạt động")
    @GetMapping
    public ResponseEntity<Page<ActivityHistoryResponse>> getAllActivityHistory(@RequestParam int page,
                                                                               @RequestParam int size) {
        Page<ActivityHistoryResponse> responses = service.getAll(PageRequest.of(page, size));
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy lịch sử hoạt động theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ActivityHistoryResponse> getActivityHistoryById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lấy lịch sử hoạt động của một công việc")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<ActivityHistoryResponse>> getComments(@PathVariable UUID taskId,
                                                                     @RequestParam int page,
                                                                     @RequestParam int size) {
        Page<ActivityHistoryResponse> responses = service.getActivityHistoryByTask(taskId, PageRequest.of(page, size));
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Tạo lịch sử hoạt động")
    @PostMapping
    public ResponseEntity<?> createActivityHistory(@RequestBody @Validated(CreateGroup.class) ActivityHistoryRequest request) {
        ActivityHistoryResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }
}
