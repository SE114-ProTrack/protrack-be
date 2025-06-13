package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.model.Project;

public class ProjectMapper {

    public static ProjectResponse toResponse(Project entity) {
        return new ProjectResponse(
                entity.getProjectId(),
                entity.getProjectName(),
                entity.getDescription(),
                entity.getCreateTime(),
                null // Nếu cần thêm creatorName sau này
        );
    }
}
