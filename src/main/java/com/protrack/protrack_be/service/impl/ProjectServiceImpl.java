package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.mapper.ProjectMapper;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.repository.ProjectRepository;
import com.protrack.protrack_be.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository repo;

    @Override
    public List<ProjectResponse> getAll(){
        return repo.findAll()
            .stream()
            .map(ProjectMapper::toResponse)
            .collect(Collectors.toList()); }

    @Override
    public Optional<ProjectResponse> getById(UUID id){
        return repo.findById(id)
            .map(ProjectMapper::toResponse);
    }

    @Override
    public ProjectResponse create(ProjectRequest request){
        return new ProjectResponse();
    }

    @Override
    public ProjectResponse update(UUID id, ProjectRequest request){
        return new ProjectResponse();
    }

    @Override
    public void delete(UUID id){ repo.deleteById(id); }
}
