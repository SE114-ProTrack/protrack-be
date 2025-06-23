package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.ActivityHistoryRequest;
import com.protrack.protrack_be.dto.response.ActivityHistoryResponse;
import com.protrack.protrack_be.model.ActivityHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityHistoryService {
    List<ActivityHistoryResponse> getAll();
    Optional<ActivityHistoryResponse> getById(UUID id);
    ActivityHistoryResponse create(ActivityHistoryRequest request);
}
