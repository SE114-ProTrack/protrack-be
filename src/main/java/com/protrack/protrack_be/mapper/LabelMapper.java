package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.LabelResponse;
import com.protrack.protrack_be.model.Label;

public class LabelMapper {

    public static LabelResponse toResponse(Label entity) {
        return new LabelResponse(
                entity.getLabelId(),
                entity.getLabelName(),
                entity.getDescription(),
                entity.getColor(),
                entity.getProject().getProjectId(),
                entity.getProject().getProjectName()
        );
    }
}
