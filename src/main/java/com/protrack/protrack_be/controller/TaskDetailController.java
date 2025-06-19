package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.TaskDetailRequest;
import com.protrack.protrack_be.dto.response.TaskDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-details")
@RequiredArgsConstructor
public class TaskDetailController {

    @GetMapping
    public ResponseEntity<List<TaskDetailResponse>> getAllTaskDetails() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{parentId}/{childId}")
    public ResponseEntity<TaskDetailResponse> getTaskDetail(
            @PathVariable UUID parentId,
            @PathVariable UUID childId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping
    public ResponseEntity<?> createTaskDetail(@RequestBody @Valid TaskDetailRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{parentId}/{childId}")
    public ResponseEntity<?> deleteTaskDetail(
            @PathVariable UUID parentId,
            @PathVariable UUID childId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
