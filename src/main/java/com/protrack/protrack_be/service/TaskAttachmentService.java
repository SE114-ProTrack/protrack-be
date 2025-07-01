package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.CommentRequest;
import com.protrack.protrack_be.dto.request.TaskAttachmentRequest;
import com.protrack.protrack_be.dto.response.CommentResponse;
import com.protrack.protrack_be.dto.response.TaskAttachmentResponse;

import java.util.List;
import java.util.UUID;

public interface TaskAttachmentService {
    TaskAttachmentResponse createTaskAttachment(TaskAttachmentRequest request);
    List<TaskAttachmentResponse> getByTaskId(UUID taskId);
    void delete(UUID id);
}