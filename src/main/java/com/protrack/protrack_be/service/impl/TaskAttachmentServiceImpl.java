package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.TaskAttachmentRequest;
import com.protrack.protrack_be.dto.response.TaskAttachmentResponse;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.TaskAttachmentMapper;
import com.protrack.protrack_be.model.Task;
import com.protrack.protrack_be.model.TaskAttachment;
import com.protrack.protrack_be.repository.TaskAttachmentRepository;
import com.protrack.protrack_be.service.TaskAttachmentService;
import com.protrack.protrack_be.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.rmi.NotBoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

    private final TaskAttachmentRepository repo;

    @Autowired
    @Lazy
    TaskService taskService;

    @Override
    public TaskAttachmentResponse createTaskAttachment(TaskAttachmentRequest request){

        Task task = taskService.getTask(request.getTaskId());

        TaskAttachment attachment = new TaskAttachment();
        attachment.setTask(task);
        attachment.setFileName(request.getFileName());
        attachment.setFileUrl(request.getFileUrl());
        attachment.setFileType(request.getFileType());
        attachment.setUploadedAt(LocalDateTime.now());
        TaskAttachment saved = repo.save(attachment);
        return TaskAttachmentMapper.toResponse(saved);
    }

    @Override
    @EnableSoftDeleteFilter
    public List<TaskAttachmentResponse> getByTaskId(UUID taskId) {
        return repo.findByTask_TaskId(taskId).stream()
                .map(attachment -> new TaskAttachmentResponse(
                        attachment.getId(),
                        attachment.getTask().getTaskId(),
                        attachment.getFileName(),
                        attachment.getFileUrl(),
                        attachment.getFileType(),
                        attachment.getUploadedAt()
                )).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Attachment not found");
        }
        repo.deleteById(id);
    }

    @Override
    public List<TaskAttachment> getByTaskIdRaw(UUID taskId) {
        return repo.findByTask_TaskId(taskId);
    }

}

