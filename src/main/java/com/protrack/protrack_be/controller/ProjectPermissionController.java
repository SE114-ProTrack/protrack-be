package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ProjectPermissionRequest;
import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import com.protrack.protrack_be.service.ProjectPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/project-permissions")
@RequiredArgsConstructor
@Tag(name = "Project Permission", description = "API quyền dự án")
public class ProjectPermissionController {

    @Autowired
    private ProjectPermissionService service;

    @Operation(summary = "Lấy tất cả quyền dự án")
    @GetMapping
    public ResponseEntity<List<ProjectPermissionResponse>> getAllPermissions() {
        List<ProjectPermissionResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/projects/{projectId}/permissions")
    @Operation(summary = "Lấy toàn bộ quyền trong 1 dự án")
    public ResponseEntity<List<ProjectPermissionResponse>> getPermissionsByProject(@PathVariable UUID projectId) {
        List<ProjectPermissionResponse> permissions = service.getByProjectId(projectId);
        return ResponseEntity.ok(permissions);
    }


    @Operation(summary = "Lấy quyền dự án theo ID dự án, ID người dùng và ID chức năng")
    @GetMapping("/{projectId}/{userId}/{functionId}")
    public ResponseEntity<ProjectPermissionResponse> getPermission(
            @PathVariable UUID projectId,
            @PathVariable UUID userId,
            @PathVariable UUID functionId) {

        ProjectPermissionId id = new ProjectPermissionId(projectId, userId, functionId);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo quyền dự án")
    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody @Valid ProjectPermissionRequest request) {
        ProjectPermissionResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/projects/{projectId}/permissions/{userId}")
    @Operation(summary = "Cập nhật quyền của người dùng trong dự án")
    public ResponseEntity<Void> updatePermission(
            @PathVariable UUID projectId,
            @PathVariable UUID userId,
            @RequestBody Map<String, Boolean> permissionUpdates
    ) {
        service.update(projectId, userId, permissionUpdates);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Xóa quyền dự án")
    @DeleteMapping("/{projectId}/{userId}/{functionId}")
    public ResponseEntity<?> deletePermission(
            @PathVariable UUID projectId,
            @PathVariable UUID userId,
            @PathVariable UUID functionId) {
        ProjectPermissionId id = new ProjectPermissionId(projectId, userId, functionId);
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
