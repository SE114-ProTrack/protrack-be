package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.TaskRequest;
import com.protrack.protrack_be.dto.response.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task", description = "API công việc")
public class TaskController {

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary = "Lấy công việc theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary = "Tạo công việc")
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary = "Cập nhật công việc")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable UUID id, @RequestBody @Valid TaskRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary = "Xóa công việc")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
