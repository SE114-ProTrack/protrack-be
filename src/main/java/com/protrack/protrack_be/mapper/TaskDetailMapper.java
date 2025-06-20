package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.TaskDetailResponse;
import com.protrack.protrack_be.model.TaskDetail;

public class TaskDetailMapper {

    public static TaskDetailResponse toResponse(TaskDetail entity) {
        return new TaskDetailResponse(
                entity.getParentTaskId().getTaskId(),
                entity.getParentTaskId().getTaskName(),
                entity.getChildTaskId().getTaskId(),
                entity.getChildTaskId().getTaskName()
        );
    }
}
