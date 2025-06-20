package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ProjectPermissionRequest;
import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import com.protrack.protrack_be.service.ProjectPermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project-permissions")
@RequiredArgsConstructor
public class ProjectPermissionController {

    @Autowired
    private ProjectPermissionService service;

    @GetMapping
    public ResponseEntity<List<ProjectPermissionResponse>> getAllPermissions() {
        List<ProjectPermissionResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

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

    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody @Valid ProjectPermissionRequest request) {
        ProjectPermissionResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}/{userId}/{functionId}")
    public ResponseEntity<?> updatePermission(
            @PathVariable UUID projectId,
            @PathVariable UUID userId,
            @PathVariable UUID functionId,
            @RequestBody @Valid ProjectPermissionRequest request) {

        ProjectPermissionId id = new ProjectPermissionId(projectId, userId, functionId);
        ProjectPermissionResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

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
