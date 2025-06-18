package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.FunctionRequest;
import com.protrack.protrack_be.dto.response.FunctionResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FunctionService {
    List<FunctionResponse> getAll();
    Optional<FunctionResponse> getById(UUID id);
    FunctionResponse create(FunctionRequest request);
    FunctionResponse update(UUID id, FunctionRequest request);
    void delete(UUID id);
}
