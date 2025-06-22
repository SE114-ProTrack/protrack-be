package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.TaskResponse;
import com.protrack.protrack_be.model.Task;

public class TaskMapper {

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getTaskId(),
                task.getTaskName(),
                task.getDescription(),
                task.getDeadline(),
                task.getPriority(),
                task.getIsMain(),
                task.getAttachment(),
                task.getProject().getProjectId(),
                task.getProject().getProjectName(),
                task.getLabel() != null ? task.getLabel().getLabelId() : null,
                task.getLabel() != null ? task.getLabel().getLabelName() : null,
                task.getRelatedTask() != null ? task.getRelatedTask().getTaskId() : null,
                task.getApprover().getUserId(),
                task.getApprover().getName(),
                task.getStatus()
        );
    }
}
