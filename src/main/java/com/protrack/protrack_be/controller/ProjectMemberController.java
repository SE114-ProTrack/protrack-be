package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ProjectMemberRequest;
import com.protrack.protrack_be.dto.response.ProjectMemberResponse;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import com.protrack.protrack_be.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project-members")
@RequiredArgsConstructor
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService service;

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getAllProjectMembers() {
        List<ProjectMemberResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{projectId}/{userId}")
    public ResponseEntity<ProjectMemberResponse> getProjectMemberById(@PathVariable UUID projectId, @PathVariable UUID userId) {
        ProjectMemberId id = new ProjectMemberId(projectId, userId);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addProjectMember(@RequestBody @Valid ProjectMemberRequest request) {
        ProjectMemberResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}/{userId}")
    public ResponseEntity<?> updateProjectMember(@PathVariable UUID projectId, @PathVariable UUID userId, @RequestBody @Valid ProjectMemberRequest request) {
        ProjectMemberId id = new ProjectMemberId(projectId, userId);
        ProjectMemberResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}/{userId}")
    public ResponseEntity<?> deleteProjectMember(@PathVariable UUID projectId, @PathVariable UUID userId) {
        ProjectMemberId id = new ProjectMemberId(projectId, userId);
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
