package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.model.Project;

public class ProjectMapper {

    public static ProjectResponse toResponse(Project entity) {
        return new ProjectResponse(
                entity.getProjectId(),
                entity.getProjectName(),
                entity.getDescription(),
                entity.getBannerUrl(),
                entity.getCreatedAt(),
                entity.getCreatorId().getName(),
                0,
                0
        );
    }
}
