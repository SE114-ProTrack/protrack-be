package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.request.TaskAttachmentRequest;
import com.protrack.protrack_be.dto.request.TaskRequest;
import com.protrack.protrack_be.dto.response.LabelResponse;
import com.protrack.protrack_be.dto.response.TaskAttachmentResponse;
import com.protrack.protrack_be.dto.response.TaskResponse;
import com.protrack.protrack_be.enums.TaskStatus;
import com.protrack.protrack_be.model.Label;
import com.protrack.protrack_be.model.Task;
import com.protrack.protrack_be.model.TaskAttachment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskResponse toResponse(Task task) {
        List<LabelResponse> labelResponses = (task.getLabels() == null)
                ? List.of()
                : task.getLabels().stream()
                .map(LabelMapper::toResponse)
                .toList();

        List<TaskAttachmentResponse> attachmentResponses = (task.getAttachment() == null)
                ? List.of()
                : task.getAttachment().stream()
                .map(TaskAttachmentMapper::toResponse)
                .toList();

        return new TaskResponse(
                task.getTaskId(),
                task.getTaskName(),
                task.getDescription(),
                task.getDeadline(),
                task.getPriority(),
                task.getIsMain(),
                attachmentResponses,
                task.getProject().getProjectId(),
                task.getProject().getProjectName(),
                labelResponses,
                task.getParentTask() != null ? task.getParentTask().getTaskId() : null,
                task.getApprover().getUserId(),
                task.getApprover().getName(),
                task.getCreatedAt(),
                task.getStatus().toString(),
                task.getIcon(),
                task.getColor()
        );
    }

    public static void toEntity(TaskRequest request, Task task, List<Label> labels, List<TaskAttachment> attachments) {
        task.setTaskName(request.getTaskName());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setPriority(request.getPriority());
        task.setIsMain(request.getIsMain());
        task.setStatus(TaskStatus.valueOf(request.getStatus().toUpperCase()));
        task.setIcon(request.getIcon());
        task.setColor(request.getColor());
        task.setLabels(labels);
        task.setAttachment(attachments);
    }

    public static List<TaskAttachment> toAttachmentEntities(List<TaskAttachmentRequest> requests, Task task) {
        if (requests == null) return List.of();
        List<TaskAttachment> list = new ArrayList<>();
        for (TaskAttachmentRequest att : requests) {
            TaskAttachment entity = new TaskAttachment();
            entity.setFileName(att.getFileName());
            entity.setFileUrl(att.getFileUrl());
            entity.setFileType(att.getFileType());
            entity.setUploadedAt(LocalDateTime.now());
            entity.setTask(task);
            list.add(entity);
        }
        return list;
    }
}
