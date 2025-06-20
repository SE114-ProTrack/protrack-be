package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.model.TaskMember;

public class TaskMemberMapper {

    public static TaskMemberResponse toResponse(TaskMember entity) {
        return new TaskMemberResponse(
                entity.getTaskId().getTaskId(),
                entity.getTaskId().getTaskName(),
                entity.getUserId().getUserId(),
                entity.getUserId().getName()
        );
    }
}
