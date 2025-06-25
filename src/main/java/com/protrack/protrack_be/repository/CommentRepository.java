package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByTask_TaskIdOrderByTimestampAsc(UUID taskId);
}

