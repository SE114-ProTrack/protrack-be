package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.LabelRequest;
import com.protrack.protrack_be.dto.response.LabelResponse;
import com.protrack.protrack_be.model.Label;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LabelService {
    List<LabelResponse> getAll();
    Optional<LabelResponse> getById(UUID id);
    List<LabelResponse> getByProject(UUID projectId);
    LabelResponse create(LabelRequest request);
    LabelResponse update(UUID id, LabelRequest request);
    void delete(UUID id);
}
