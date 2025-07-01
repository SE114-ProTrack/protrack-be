package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.response.TaskAttachmentResponse;
import com.protrack.protrack_be.repository.TaskAttachmentRepository;
import com.protrack.protrack_be.service.TaskAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

    private final TaskAttachmentRepository repo;

    @Override
    public List<TaskAttachmentResponse> getByTaskId(UUID taskId) {
        return repo.findByTask_TaskId(taskId).stream()
                .map(attachment -> new TaskAttachmentResponse(
                        attachment.getId(),
                        attachment.getFileName(),
                        attachment.getFileUrl(),
                        attachment.getFileType(),
                        attachment.getUploadedAt()
                )).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Attachment không tồn tại");
        }
        repo.deleteById(id);
    }
}

