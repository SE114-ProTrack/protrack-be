package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.ActivityHistoryRequest;
import com.protrack.protrack_be.dto.response.ActivityHistoryResponse;
import com.protrack.protrack_be.model.ActivityHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityHistoryService {
    Page<ActivityHistoryResponse> getAll(Pageable pageable);
    Optional<ActivityHistoryResponse> getById(UUID id);
    Page<ActivityHistoryResponse> getActivityHistoryByTask(UUID taskId, Pageable pageable);
    ActivityHistoryResponse create(ActivityHistoryRequest request);
}
