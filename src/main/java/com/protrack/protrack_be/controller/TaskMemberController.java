package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.TaskMemberRequest;
import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-members")
@RequiredArgsConstructor
public class TaskMemberController {

    @GetMapping
    public ResponseEntity<List<TaskMemberResponse>> getAllTaskMembers() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{taskId}/{userId}")
    public ResponseEntity<TaskMemberResponse> getTaskMember(
            @PathVariable UUID taskId,
            @PathVariable UUID userId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping
    public ResponseEntity<?> addTaskMember(@RequestBody @Valid TaskMemberRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{taskId}/{userId}")
    public ResponseEntity<?> deleteTaskMember(
            @PathVariable UUID taskId,
            @PathVariable UUID userId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
