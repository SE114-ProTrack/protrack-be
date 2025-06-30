package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.response.TaskAttachmentResponse;

import java.util.List;
import java.util.UUID;

public interface TaskAttachmentService {
    List<TaskAttachmentResponse> getByTaskId(UUID taskId);
    void delete(UUID id);
}