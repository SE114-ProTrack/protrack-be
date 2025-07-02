package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    Page<ProjectResponse> getAll(Pageable pageable);
    Optional<ProjectResponse> getById(UUID id);
    Optional<Project> getEntityById(UUID id);
    ProjectResponse create(ProjectRequest request);
    ProjectResponse update(UUID id, ProjectRequest request);
    void delete(UUID id);
    Page<ProjectResponse> getProjectsByUser(UUID userId, Pageable pageable);
    List<ProjectResponse> get3ByUser(UUID userId);
    List<ProjectResponse> findByKeyword(String keyword);
    ProjectResponse updateProjectBanner(UUID projectId, String bannerUrl);
    boolean hasProjectRight(UUID projectId, UUID userId, ProjectFunctionCode function);
}
