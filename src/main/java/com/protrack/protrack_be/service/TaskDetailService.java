package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.TaskDetailRequest;
import com.protrack.protrack_be.dto.response.TaskDetailResponse;
import com.protrack.protrack_be.model.id.TaskDetailId;

import java.util.List;
import java.util.Optional;

public interface TaskDetailService {
    List<TaskDetailResponse> getAll();
    Optional<TaskDetailResponse> getById(TaskDetailId id);
    TaskDetailResponse create(TaskDetailRequest request);
    void delete(TaskDetailId id);
}
