package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.model.TaskMember;

public class TaskMemberMapper {

    public static TaskMemberResponse toResponse(TaskMember entity) {
        return new TaskMemberResponse(
                entity.getTask().getTaskId(),
                entity.getTask().getTaskName(),
                entity.getUser().getUserId(),
                entity.getUser().getName()
        );
    }
}
