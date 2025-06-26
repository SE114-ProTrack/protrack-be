package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProject_ProjectId(UUID projectId);
    boolean existsByParentTask_TaskId(UUID taskId);
}

