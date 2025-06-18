package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.ProjectPermissionRequest;
import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.mapper.ProjectMemberMapper;
import com.protrack.protrack_be.mapper.ProjectPermissionMapper;
import com.protrack.protrack_be.model.ProjectPermission;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import com.protrack.protrack_be.repository.ProjectPermissionRepository;
import com.protrack.protrack_be.service.ProjectPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectPermissionServiceImpl implements ProjectPermissionService {
    @Autowired
    ProjectPermissionRepository repo;

    @Override
    public List<ProjectPermissionResponse> getAll(){
        return repo.findAll().stream()
            .map(ProjectPermissionMapper::toResponse)
            .collect(Collectors.toList()); }

    @Override
    public Optional<ProjectPermissionResponse> getById(ProjectPermissionId id){
        return repo.findById(id)
                .map(ProjectPermissionMapper::toResponse);
    }

    @Override
    public ProjectPermissionResponse create(ProjectPermissionRequest request){
        return new ProjectPermissionResponse();
    }

    @Override
    public ProjectPermissionResponse update(ProjectPermissionId id, ProjectPermissionRequest request){
        return new ProjectPermissionResponse();
    }

    @Override
    public void delete(ProjectPermissionId id){ repo.deleteById(id); }
}
