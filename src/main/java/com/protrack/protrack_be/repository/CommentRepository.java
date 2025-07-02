package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByTask_TaskIdOrderByCreatedAtAsc(UUID taskId, Pageable pageable);
}

