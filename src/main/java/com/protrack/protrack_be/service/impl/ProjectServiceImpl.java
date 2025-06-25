package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.dto.response.UserResponse;
import com.protrack.protrack_be.mapper.ProjectMapper;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.ProjectRepository;
import com.protrack.protrack_be.service.ProjectService;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.ProjectMapper.toResponse;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectRepository repo;

    @Autowired
    UserService userService;

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
    public Optional<Project> getEntityById(UUID id){
        return repo.findById(id);
    }

    @Override
    public ProjectResponse create(ProjectRequest request){
        Project project = new Project();
        User user = userService.getCurrentUser();

        project.setCreatorId(user);
        project.setProjectName(request.getProjectName());
        project.setCreateTime(LocalDateTime.now());
        project.setDescription(request.getDescription());

        Project saved = repo.save(project);

        return toResponse(saved);
    }

    @Override
    public ProjectResponse update(UUID id, ProjectRequest request){
        Project project = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find project"));

        if(request.getProjectName() != null) project.setProjectName(request.getProjectName());
        if(request.getDescription() != null) project.setDescription(request.getDescription());

        Project saved = repo.save(project);

        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){ repo.deleteById(id); }
}
