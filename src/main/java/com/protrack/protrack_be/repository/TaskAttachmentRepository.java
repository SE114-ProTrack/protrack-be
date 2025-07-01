package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, UUID> {
    List<TaskAttachment> findByTask_TaskId(UUID taskId);
    void deleteByTask_TaskId(UUID taskId);
}

