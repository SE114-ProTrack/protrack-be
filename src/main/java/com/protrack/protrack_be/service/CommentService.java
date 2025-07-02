package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.CommentRequest;
import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.CommentResponse;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {
    CommentResponse createComment(CommentRequest request);
    Page<CommentResponse> getCommentsByTask(UUID taskId, Pageable pageable);
    Optional<CommentResponse> getById(UUID id);
    CommentResponse update(UUID id, CommentRequest request);
    void delete(UUID id);
}
