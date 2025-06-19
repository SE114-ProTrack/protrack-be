package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.TaskDetail;
import com.protrack.protrack_be.model.id.TaskDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDetailRepository extends JpaRepository<TaskDetail, TaskDetailId> {}

