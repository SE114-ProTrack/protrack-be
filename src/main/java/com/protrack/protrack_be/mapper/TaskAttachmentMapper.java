package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.TaskAttachmentResponse;
import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.model.TaskAttachment;
import com.protrack.protrack_be.model.TaskMember;

public class TaskAttachmentMapper {
    public static TaskAttachmentResponse toResponse(TaskAttachment entity) {
        return new TaskAttachmentResponse(
                entity.getId(),
                entity.getTask().getTaskId(),
                entity.getFileName(),
                entity.getFileUrl(),
                entity.getFileType(),
                entity.getUploadedAt()
        );
    }
}
