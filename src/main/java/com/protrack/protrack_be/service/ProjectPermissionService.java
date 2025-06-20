package com.protrack.protrack_be.service;

import com.protrack.protrack_be.enums.ProjectFunctionCode;

import java.util.UUID;

public interface ProjectPermissionService {
    boolean hasPermission(UUID userId, UUID projectId, ProjectFunctionCode functionCode);

}
