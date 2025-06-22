package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.TaskDetailRequest;
import com.protrack.protrack_be.dto.response.TaskDetailResponse;
import com.protrack.protrack_be.model.TaskDetail;
import com.protrack.protrack_be.model.id.TaskDetailId;
import com.protrack.protrack_be.service.TaskDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-details")
@RequiredArgsConstructor
public class TaskDetailController {

    @Autowired
    TaskDetailService service;

    @GetMapping
    public ResponseEntity<List<TaskDetailResponse>> getAllTaskDetails() {
        List<TaskDetailResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{parentId}/{childId}")
    public ResponseEntity<TaskDetailResponse> getTaskDetail(
            @PathVariable UUID parentId,
            @PathVariable UUID childId) {
        TaskDetailId id = new TaskDetailId(parentId, childId);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTaskDetail(@RequestBody @Valid TaskDetailRequest request) {
        TaskDetailResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{parentId}/{childId}")
    public ResponseEntity<?> deleteTaskDetail(
            @PathVariable UUID parentId,
            @PathVariable UUID childId) {
        TaskDetailId id = new TaskDetailId(parentId, childId);
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
