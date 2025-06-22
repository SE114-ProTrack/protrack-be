package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.model.ProjectPermission;

public class ProjectPermissionMapper {
    public static ProjectPermissionResponse toResponse(ProjectPermission entity) {
        return new ProjectPermissionResponse(
                entity.getProjectId().getProjectId(),
                entity.getUserId().getUserId(),
                entity.getFunctionId().getFunctionId(),
                entity.getFunctionId().getFunctionName(),
                entity.getIsActive()
        );
    }
}
