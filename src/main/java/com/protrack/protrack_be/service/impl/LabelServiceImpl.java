package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.LabelRequest;
import com.protrack.protrack_be.dto.response.LabelResponse;
import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.LabelMapper;
import com.protrack.protrack_be.model.Label;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.repository.LabelRepository;
import com.protrack.protrack_be.service.LabelService;
import com.protrack.protrack_be.service.ProjectPermissionService;
import com.protrack.protrack_be.service.ProjectService;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.LabelMapper.toResponse;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    LabelRepository repo;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectPermissionService projectPermissionService;

    @Override
    @EnableSoftDeleteFilter
    public List<LabelResponse> getAll(){
        return repo.findAll()
                .stream()
                .map(LabelMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<LabelResponse> getById(UUID id){
        return repo.findById(id)
                .map(LabelMapper::toResponse);
    }

    @Override
    public List<LabelResponse> getByProject(UUID projectId) {
        Project project = projectService.getEntityById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        UUID userId = userService.getCurrentUser().getUserId();
        if (projectService.hasProjectRight(project.getProjectId(), userId, ProjectFunctionCode.VIEW_TASK)) {
            throw new com.protrack.protrack_be.exception.AccessDeniedException("You are not permitted to view labels of this project");
        }
        return repo.findByProject_ProjectId(projectId)
                .stream()
                .map(LabelMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public LabelResponse create(LabelRequest request){
        Project project = projectService.getEntityById(request.getProjectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));
        UUID userId = userService.getCurrentUser().getUserId();

        if (projectService.hasProjectRight(project.getProjectId(), userId, ProjectFunctionCode.EDIT_PROJECT)) {
            throw new AccessDeniedException("You are not permitted to manage labels in this project");
        }

        if (repo.existsByProjectAndLabelName(project, request.getLabelName())) {
            throw new BadRequestException("Label name already exists in this project");
        }

        Label label = new Label();
        label.setLabelName(request.getLabelName());
        label.setDescription(request.getDescription());
        label.setProject(project);
        label.setColor(request.getColor());

        Label saved = repo.save(label);

        return toResponse(saved);
    }

    @Override
    @EnableSoftDeleteFilter
    public LabelResponse update(UUID id, LabelRequest request){
        Project project = projectService.getEntityById(request.getProjectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));
        UUID userId = userService.getCurrentUser().getUserId();

        if (projectService.hasProjectRight(project.getProjectId(), userId, ProjectFunctionCode.EDIT_PROJECT)) {
            throw new AccessDeniedException("You are not permitted to manage labels in this project");
        }

        Label label = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Label not found"));

        if (request.getLabelName() != null
                && repo.existsByProjectAndLabelName(project, request.getLabelName())
                && !label.getLabelName().equals(request.getLabelName())) {
            throw new BadRequestException("Label name already exists in this project");
        }
        if(request.getDescription() != null) label.setDescription(request.getDescription());
        if(request.getColor() != null) label.setColor(request.getColor());

        Label saved = repo.save(label);

        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){
        Label label = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find label"));;
        Project project = label.getProject();
        UUID userId = userService.getCurrentUser().getUserId();

        if (projectService.hasProjectRight(project.getProjectId(), userId, ProjectFunctionCode.EDIT_PROJECT)) {
            throw new AccessDeniedException("You are not permitted to manage labels in this project");
        }

        repo.deleteById(id);
    }
}
