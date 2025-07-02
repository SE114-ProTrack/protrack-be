package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.FunctionRequest;
import com.protrack.protrack_be.dto.response.FunctionResponse;
import com.protrack.protrack_be.model.Function;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FunctionService {
    List<FunctionResponse> getAll();
    List<Function> getDefaults();
    List<Function> getAllEntities();
    Optional<FunctionResponse> getById(UUID id);
    Optional<Function> getEntityById(UUID id);
    Optional<Function> getEntityByFunctionCode(String functionCode);
    FunctionResponse create(FunctionRequest request);
    FunctionResponse update(UUID id, FunctionRequest request);
    void delete(UUID id);
}
