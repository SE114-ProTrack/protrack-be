package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.request.TaskRequest;
import com.protrack.protrack_be.dto.response.TaskResponse;
import com.protrack.protrack_be.model.Task;
import com.protrack.protrack_be.model.TaskAttachment;

import java.util.List;
import java.util.stream.Collectors;

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
                task.getParentTask() != null ? task.getParentTask().getTaskId() : null,
                task.getApprover().getUserId(),
                task.getApprover().getName(),
                task.getCreatedAt(),
                task.getStatus(),
                task.getIcon(),
                task.getColor()
        );
    }

    public static Task toEntity(TaskRequest request, Task task) {
        // Cập nhật thông tin cơ bản cho task
        task.setTaskName(request.getTaskName());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setPriority(request.getPriority());
        task.setIsMain(request.getIsMain());
        task.setStatus(request.getStatus());
        task.setIcon(request.getIcon());
        task.setColor(request.getColor());

        // Lưu các TaskAttachment
        if (request.getAttachments() != null) {
            List<TaskAttachment> attachments = request.getAttachments().stream().map(att -> {
                TaskAttachment attachment = new TaskAttachment();
                attachment.setFileName(att.getFileName());
                attachment.setFileUrl(att.getFileUrl());
                attachment.setFileType(att.getFileType());
                attachment.setTask(task);
                return attachment;
            }).collect(Collectors.toList());

            task.setAttachment(attachments);  // Set danh sách các attachment vào task
        }

        return task;
    }
}
