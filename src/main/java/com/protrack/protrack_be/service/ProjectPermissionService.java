package com.protrack.protrack_be.service;

import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.dto.request.ProjectPermissionRequest;
import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.model.id.ProjectPermissionId;

import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProjectPermissionService {
    boolean hasPermission(UUID userId, UUID projectId, ProjectFunctionCode functionCode);

    List<ProjectPermissionResponse> getAll();
    List<ProjectPermissionResponse> getByProjectId(UUID projectId);
    Optional<ProjectPermissionResponse> getById(ProjectPermissionId id);
    ProjectPermissionResponse create(ProjectPermissionRequest request);
    void update(UUID projectId, UUID userId, Map<String, Boolean> permissionMap);
    void delete(ProjectPermissionId id);
}
