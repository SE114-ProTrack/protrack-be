package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.TaskMemberRequest;
import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.model.id.TaskMemberId;
import com.protrack.protrack_be.service.TaskMemberService;
import com.protrack.protrack_be.validation.CreateGroup;
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
@RequestMapping("/api/task-members")
@RequiredArgsConstructor
@Tag(name = "Task Member", description = "API thành viên công việc")
public class TaskMemberController {

    @Autowired
    TaskMemberService service;

    @Operation(summary = "Lấy tất cả thành viên công việc")
    @GetMapping
    public ResponseEntity<List<TaskMemberResponse>> getAllTaskMembers() {
        List<TaskMemberResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy thành viên công việc theo ID")
    @GetMapping("/{taskId}/{userId}")
    public ResponseEntity<TaskMemberResponse> getTaskMember(
            @PathVariable UUID taskId,
            @PathVariable UUID userId) {
        TaskMemberId id = new TaskMemberId(taskId, userId);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Thêm thành viên công việc")
    @PostMapping
    public ResponseEntity<?> addTaskMember(@RequestBody @Validated(CreateGroup.class) TaskMemberRequest request) {
        TaskMemberResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa thành viên công việc")
    @DeleteMapping("/{taskId}/{userId}")
    public ResponseEntity<?> deleteTaskMember(
            @PathVariable UUID taskId,
            @PathVariable UUID userId) {
        TaskMemberId id = new TaskMemberId(taskId, userId);
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
