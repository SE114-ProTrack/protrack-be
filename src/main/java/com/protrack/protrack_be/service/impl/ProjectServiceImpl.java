package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.ProjectMapper;
import com.protrack.protrack_be.model.*;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import com.protrack.protrack_be.repository.FunctionRepository;
import com.protrack.protrack_be.repository.ProjectMemberRepository;
import com.protrack.protrack_be.repository.ProjectPermissionRepository;
import com.protrack.protrack_be.repository.ProjectRepository;
import com.protrack.protrack_be.service.ProjectMemberService;
import com.protrack.protrack_be.service.ProjectPermissionService;
import com.protrack.protrack_be.service.ProjectService;
import com.protrack.protrack_be.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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

    @Autowired
    @Lazy
    ProjectMemberService projectMemberService;
    @Autowired
    @Lazy
    ProjectPermissionService projectPermissionService;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ProjectPermissionRepository projectPermissionRepository;

    @Autowired
    FunctionRepository functionRepository;

    @Override
    @EnableSoftDeleteFilter
    public Page<ProjectResponse> getAll(Pageable pageable){
        return repo.findAll(pageable)
                .map(ProjectMapper::toResponse);
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<ProjectResponse> getById(UUID id){
        return repo.findById(id)
            .map(ProjectMapper::toResponse);
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<Project> getEntityById(UUID id){
        return repo.findById(id);
    }

    @Override
    @Transactional
    public ProjectResponse create(ProjectRequest request){
        Project project = new Project();
        User user = userService.getCurrentUser();

        project.setCreatorId(user);
        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setBannerUrl(request.getBannerUrl());

        Project saved = repo.save(project);

        // Add creator to project member
        ProjectMember projectMember = new ProjectMember();
        ProjectMemberId projectMemberId = new ProjectMemberId(saved.getProjectId(), user.getUserId());
        projectMember.setId(projectMemberId);

        projectMember.setProject(saved);
        projectMember.setUser(user);
        projectMember.setIsProjectOwner(true);
        projectMember.setRole("Owner");
        projectMemberRepository.save(projectMember);

        projectPermissionService.grantAllPermissions(user.getUserId(), saved.getProjectId());

        return ProjectMapper.toResponse(saved);
    }

    @Override
    @EnableSoftDeleteFilter
    public ProjectResponse update(UUID id, ProjectRequest request){
        Project project = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find project"));
        User user = userService.getCurrentUser();

        if (hasProjectRight(project.getProjectId(), user.getUserId(), ProjectFunctionCode.EDIT_PROJECT)) {
            throw new AccessDeniedException("You are not permitted to edit this project");
        }

        if(request.getProjectName() != null) project.setProjectName(request.getProjectName());
        if(request.getDescription() != null) project.setDescription(request.getDescription());

        Project saved = repo.save(project);

        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){
        if (!projectMemberService.isProjectOwner(id, userService.getCurrentUser().getUserId())) {
            throw new AccessDeniedException("You are not permitted to delete this project");
        }
        repo.deleteById(id);
    }

    @Override
    @EnableSoftDeleteFilter
    public Page<ProjectResponse> getProjectsByUser(UUID userId, Pageable pageable){
        return repo.findProjectsByUserId(userId, pageable)
                .map(project -> {
                    ProjectResponse res = ProjectMapper.toResponse(project);

                    res.setAllTasks(repo.getNumberOfTasks(project.getProjectId()));
                    res.setCompletedTasks(repo.getNumberOfCompletedTasks(project.getProjectId()));

                    return res;
                });
    }

    @Override
    public List<ProjectResponse> get3ByUser(UUID userId){
        return repo.findTop3ProjectsByUserId(userId)
                .stream()
                .map(project -> {
                    ProjectResponse res = ProjectMapper.toResponse(project);

                    res.setAllTasks(repo.getNumberOfTasks(project.getProjectId()));
                    res.setCompletedTasks(repo.getNumberOfCompletedTasks(project.getProjectId()));

                    return res;
                })
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public List<ProjectResponse> findByKeyword(String keyword) {
        return repo.findByProjectNameContainingIgnoreCase(keyword)
                .stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse updateProjectBanner(UUID projectId, String bannerUrl){
        Project project = repo.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found with id: " + projectId));

        project.setBannerUrl(bannerUrl);
        Project saved = repo.save(project);
        return toResponse(saved);
    }

    @Override
    @EnableSoftDeleteFilter
    public boolean hasProjectRight(UUID projectId, UUID userId, ProjectFunctionCode function) {
        return !projectMemberService.isProjectOwner(projectId, userId)
                && !projectPermissionService.hasPermission(userId, projectId, function);
    }

    @Override
    public List<ProjectResponse> getCanCreateTask(){
        return repo.findAbleToAddTask(userService.getCurrentUser().getUserId())
                .stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }

}
