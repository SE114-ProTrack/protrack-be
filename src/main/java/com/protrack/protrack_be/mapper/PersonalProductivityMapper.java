package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.PersonalProductivityResponse;
import com.protrack.protrack_be.model.PersonalProductivity;

public class PersonalProductivityMapper {

    public static PersonalProductivityResponse toResponse(PersonalProductivity entity) {
        return new PersonalProductivityResponse(
                entity.getUser().getUserId(),
                entity.getUser().getName(),
                entity.getProject().getProjectId(),
                entity.getProject().getProjectName(),
                entity.getCompletedTasks(),
                entity.getLastUpdated()
        );
    }
}
