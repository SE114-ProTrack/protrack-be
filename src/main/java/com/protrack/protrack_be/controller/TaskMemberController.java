package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.TaskMemberRequest;
import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.model.id.TaskMemberId;
import com.protrack.protrack_be.service.TaskMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-members")
@RequiredArgsConstructor
public class TaskMemberController {

    @Autowired
    TaskMemberService service;

    @GetMapping
    public ResponseEntity<List<TaskMemberResponse>> getAllTaskMembers() {
        List<TaskMemberResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{taskId}/{userId}")
    public ResponseEntity<TaskMemberResponse> getTaskMember(
            @PathVariable UUID taskId,
            @PathVariable UUID userId) {
        TaskMemberId id = new TaskMemberId(taskId, userId);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addTaskMember(@RequestBody @Valid TaskMemberRequest request) {
        TaskMemberResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}/{userId}")
    public ResponseEntity<?> deleteTaskMember(
            @PathVariable UUID taskId,
            @PathVariable UUID userId) {
        TaskMemberId id = new TaskMemberId(taskId, userId);
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
