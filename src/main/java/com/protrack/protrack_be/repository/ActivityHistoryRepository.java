package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.ActivityHistory;
import com.protrack.protrack_be.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, UUID> {
    void deleteByTask_TaskId(UUID taskId);
    Page<ActivityHistory> findByTask_TaskIdOrderByCreatedAtAsc(UUID taskId, Pageable pageable);
}

