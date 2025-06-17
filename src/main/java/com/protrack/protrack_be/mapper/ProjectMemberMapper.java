package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.ProjectMemberResponse;
import com.protrack.protrack_be.model.ProjectMember;

public class ProjectMemberMapper {
    public static ProjectMemberResponse toResponse(ProjectMember entity) {
        return new ProjectMemberResponse(
                entity.getProjectId().getProjectId(),
                entity.getUserId().getUserId(),
                entity.getUserId().getName(),
                entity.getIsProjectOwner()
        );
    }
}
