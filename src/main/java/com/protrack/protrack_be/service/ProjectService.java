package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.model.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    List<ProjectResponse> getAll();
    Optional<ProjectResponse> getById(UUID id);
    Optional<Project> getEntityById(UUID id);
    ProjectResponse create(ProjectRequest request);
    ProjectResponse update(UUID id, ProjectRequest request);
    void delete(UUID id);
}
