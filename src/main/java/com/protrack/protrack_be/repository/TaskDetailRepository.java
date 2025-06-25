package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.TaskDetail;
import com.protrack.protrack_be.model.id.TaskDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskDetailRepository extends JpaRepository<TaskDetail, TaskDetailId> {
    void deleteByParentTask_TaskId(UUID taskId);
    void deleteByChildTask_TaskId(UUID taskId);
}

