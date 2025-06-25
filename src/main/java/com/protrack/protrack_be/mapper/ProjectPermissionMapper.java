package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.model.ProjectPermission;

public class ProjectPermissionMapper {
    public static ProjectPermissionResponse toResponse(ProjectPermission entity) {
        return new ProjectPermissionResponse(
                entity.getProject().getProjectId(),
                entity.getUser().getUserId(),
                entity.getFunction().getFunctionId(),
                entity.getFunction().getFunctionName(),
                entity.getIsActive()
        );
    }
}
