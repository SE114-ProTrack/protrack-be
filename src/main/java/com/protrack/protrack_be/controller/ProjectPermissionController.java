package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ProjectPermissionRequest;
import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-permissions")
@RequiredArgsConstructor
public class ProjectPermissionController {

    @GetMapping
    public ResponseEntity<List<ProjectPermissionResponse>> getAllPermissions() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{projectId}/{userId}/{functionId}")
    public ResponseEntity<ProjectPermissionResponse> getPermission(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @PathVariable Long functionId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody @Valid ProjectPermissionRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/{projectId}/{userId}/{functionId}")
    public ResponseEntity<?> updatePermission(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @PathVariable Long functionId,
            @RequestBody @Valid ProjectPermissionRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{projectId}/{userId}/{functionId}")
    public ResponseEntity<?> deletePermission(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @PathVariable Long functionId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
