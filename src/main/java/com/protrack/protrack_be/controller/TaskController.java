package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.TaskRequest;
import com.protrack.protrack_be.dto.request.TaskStatusRequest;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.dto.response.TaskResponse;
import com.protrack.protrack_be.model.Task;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.service.TaskService;
import com.protrack.protrack_be.service.UserService;
import com.protrack.protrack_be.validation.CreateGroup;
import com.protrack.protrack_be.validation.UpdateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task", description = "API công việc")
public class TaskController {

    @Autowired
    TaskService service;
    @Autowired
    UserService userService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getAllTasksByProject(@PathVariable UUID projectId) {
        User user = userService.getCurrentUser();
        List<TaskResponse> responses = service.getTasks(projectId, user.getUserId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy công việc theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
        User user = userService.getCurrentUser();
        return service.getTaskById(id, user.getUserId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo công việc")
    @PostMapping
    public ResponseEntity<?> createTask(@Validated(CreateGroup.class) @RequestBody TaskRequest request) {
        User user = userService.getCurrentUser();
        TaskResponse response = service.createTask(request, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật công việc")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskRequest request) {
        User user = userService.getCurrentUser();
        TaskResponse response = service.updateTask(id, request, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật trạng thái công việc")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable UUID id, @RequestBody TaskStatusRequest request) {
        User user = userService.getCurrentUser();
        TaskResponse response = service.updateTaskStatus(id, request, user.getUserId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa công việc")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID id) {
        User user = userService.getCurrentUser();
        service.deleteTask(id, user.getUserId());
        return ResponseEntity.ok("Delete task successfully");
    }

    @Operation(summary = "Lấy tất cả công việc theo user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable UUID userId){
        List<TaskResponse> responses = service.getTasksByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy 3 công việc tạo gần nhất theo user")
    @GetMapping("/user/{userId}/get3")
    public ResponseEntity<?> getTop3ByUser(@PathVariable UUID userId){
        List<TaskResponse> responses = service.get3ByUser(userId);
        return ResponseEntity.ok(responses);
    }
}
