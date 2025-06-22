package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.ActivityHistoryResponse;
import com.protrack.protrack_be.model.ActivityHistory;

public class ActivityHistoryMapper {

    public static ActivityHistoryResponse toResponse(ActivityHistory entity) {
        return new ActivityHistoryResponse(
                entity.getId(),
                entity.getUser().getUserId(),
                entity.getUser().getName(),
                entity.getTask().getTaskId(),
                entity.getTask().getTaskName(),
                entity.getActionType(),
                entity.getDescription(),
                entity.getTimestamp()
        );
    }
}
