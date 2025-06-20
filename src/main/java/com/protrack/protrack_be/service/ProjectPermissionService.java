package com.protrack.protrack_be.service;

import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.dto.request.ProjectPermissionRequest;
import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.model.id.ProjectPermissionId;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProjectPermissionService {
    boolean hasPermission(UUID userId, UUID projectId, ProjectFunctionCode functionCode);

    List<ProjectPermissionResponse> getAll();
    Optional<ProjectPermissionResponse> getById(ProjectPermissionId id);
    ProjectPermissionResponse create(ProjectPermissionRequest request);
    ProjectPermissionResponse update(ProjectPermissionId id, ProjectPermissionRequest request);
    void delete(ProjectPermissionId id);
}
