package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ProjectMemberRequest;
import com.protrack.protrack_be.dto.response.ProjectMemberResponse;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import com.protrack.protrack_be.service.ProjectMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Project Member", description = "API thành viên dự án")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService service;

    @Operation(summary = "Lấy tất cả thành viên dự án")
    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getAllProjectMembers() {
        List<ProjectMemberResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy thành viên dự án theo ID dự án và ID người dùng")
    @GetMapping("/{projectId}/{userId}")
    public ResponseEntity<ProjectMemberResponse> getProjectMemberById(@PathVariable UUID projectId, @PathVariable UUID userId) {
        ProjectMemberId id = new ProjectMemberId(projectId, userId);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Thêm thành viên dự án")
    @PostMapping
    public ResponseEntity<?> addProjectMember(@RequestBody @Valid ProjectMemberRequest request) {
        ProjectMemberResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật thành viên dự án")
    @PutMapping("/{projectId}/{userId}")
    public ResponseEntity<?> updateProjectMember(@PathVariable UUID projectId, @PathVariable UUID userId, @RequestBody @Valid ProjectMemberRequest request) {
        ProjectMemberId id = new ProjectMemberId(projectId, userId);
        ProjectMemberResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa thành viên dự án")
    @DeleteMapping("/{projectId}/{userId}")
    public ResponseEntity<?> deleteProjectMember(@PathVariable UUID projectId, @PathVariable UUID userId) {
        ProjectMemberId id = new ProjectMemberId(projectId, userId);
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
