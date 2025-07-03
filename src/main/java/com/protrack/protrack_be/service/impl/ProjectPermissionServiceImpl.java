package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.dto.request.ProjectPermissionRequest;
import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.exception.AccessDeniedException;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.mapper.ProjectPermissionMapper;
import com.protrack.protrack_be.model.Function;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.ProjectPermission;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import com.protrack.protrack_be.repository.ProjectPermissionRepository;
import com.protrack.protrack_be.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;
import static com.protrack.protrack_be.mapper.ProjectPermissionMapper.toResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectPermissionServiceImpl implements ProjectPermissionService {
    @Autowired
    ProjectPermissionRepository repo;

    @Autowired
    ProjectService projectService;

    private final ProjectPermissionRepository permissionRepository;
    @Autowired
    UserService userService;

    @Autowired
    FunctionService functionService;

    @Autowired
            @Lazy
    ProjectMemberService projectMemberService;

    @Override
    public List<ProjectPermissionResponse> getAll(){
        return repo.findAll().stream()
            .map(ProjectPermissionMapper::toResponse)
            .collect(Collectors.toList()); }

    @Override
    public List<ProjectPermissionResponse> getByProjectId(UUID projectId) {
        return repo.findByProject_ProjectId(projectId).stream()
                .map(ProjectPermissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectPermissionResponse> getById(ProjectPermissionId id){
        return repo.findById(id)
                .map(ProjectPermissionMapper::toResponse);
    }

    @Override
    public ProjectPermissionResponse create(ProjectPermissionRequest request){
        ProjectPermission projectPermission = new ProjectPermission();
        Project project = projectService.getEntityById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Can not find project"));
        User user = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Can not find user"));
        Function function = functionService.getEntityById(request.getFunctionId())
                .orElseThrow(() -> new RuntimeException("Can not find function"));

        if (!projectMemberService.isMember(project.getProjectId(), user.getUserId())) {
            throw new BadRequestException("User is not a member of this project.");
        }

        if (!functionService.existsById(function.getFunctionId())) {
            throw new BadRequestException("Function ID is invalid.");
        }

        ProjectPermissionId id = new ProjectPermissionId(project.getProjectId(), user.getUserId(), function.getFunctionId());
        Optional<ProjectPermission> existing = repo.findById(id);
        if (existing.isPresent()) {
            throw new BadRequestException("Permission already exists. Use update instead.");
        }
        projectPermission.setId(id);
        projectPermission.setProject(project);
        projectPermission.setUser(user);
        projectPermission.setFunction(function);
        projectPermission.setIsActive(request.getIsActive());

        ProjectPermission saved = repo.save(projectPermission);

        return toResponse(saved);
    }

    @Override
    public void update(UUID projectId, UUID userId, Map<String, Boolean> permissionMap) {
        if (projectService.hasProjectRight(projectId, userService.getCurrentUser().getUserId(), ProjectFunctionCode.EDIT_MEMBER)) {
            throw new AccessDeniedException("You are not permitted to manage project permissions.");
        }

        if (!projectMemberService.isMember(projectId, userId)) {
            throw new BadRequestException("User is not a member of this project.");
        }

        if (projectMemberService.isProjectOwner(projectId, userId)) {
            throw new AccessDeniedException("You are not permitted to change permissions of the project owner.");
        }

        for (Map.Entry<String, Boolean> entry : permissionMap.entrySet()) {
            String functionCode = entry.getKey();
            Boolean isActive = entry.getValue();

            Function func = functionService.getEntityByFunctionCode(functionCode)
                    .orElseThrow(() -> new RuntimeException("Function not found"));

            ProjectPermissionId id = new ProjectPermissionId(projectId, userId, func.getFunctionId());
            ProjectPermission permission = repo.findById(id).orElse(new ProjectPermission(id, projectService.getEntityById(projectId).get(), userService.getUserById(userId).get(), func, false));
            permission.setIsActive(isActive);
            repo.save(permission);
        }
    }


    @Override
    public void delete(ProjectPermissionId id){
        ProjectPermission projectPermission = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project Permission not found"));

        if (projectService.hasProjectRight(projectPermission.getId().getProjectId(), userService.getCurrentUser().getUserId(), ProjectFunctionCode.EDIT_MEMBER)) {
            throw new AccessDeniedException("You are not permitted to manage project permissions.");
        }

        if (!projectMemberService.isMember(projectPermission.getProject().getProjectId(), projectPermission.getUser().getUserId())) {
            throw new BadRequestException("User is not a member of this project.");
        }

        if (projectMemberService.isProjectOwner(projectPermission.getProject().getProjectId(), projectPermission.getUser().getUserId())) {
            throw new AccessDeniedException("You are not permitted to change permissions of the project owner.");
        }

        repo.deleteById(id);
    }

    @Override
    public boolean hasPermission(UUID userId, UUID projectId, ProjectFunctionCode functionCode) {
        return permissionRepository.existsByUser_UserIdAndProject_ProjectIdAndFunction_FunctionCodeAndIsActiveTrue(
                userId, projectId, functionCode.name()
        );
    }

    @Override
    public void grantDefaultPermissions(UUID userId, UUID projectId) {
        List<Function> defaults = functionService.getDefaults();
        for (Function func : defaults) {
            ProjectPermissionId id = new ProjectPermissionId(projectId, userId, func.getFunctionId());
            ProjectPermission permission = new ProjectPermission(id, projectService.getEntityById(projectId).get(), userService.getUserById(userId).get(), func, true);
            repo.save(permission);
        }
    }

    @Override
    public void grantAllPermissions(UUID userId, UUID projectId) {
        List<Function> all = functionService.getAllEntities();
        for (Function func : all) {
            ProjectPermissionId id = new ProjectPermissionId(projectId, userId, func.getFunctionId());
            ProjectPermission permission = new ProjectPermission(id, projectService.getEntityById(projectId).get(), userService.getUserById(userId).get(), func, true);
            repo.save(permission);
        }
    }

    @Override
    public void deleteAllPermissionsOfUserInProject(UUID projectId, UUID userId) {
        repo.deleteByProject_ProjectIdAndUser_UserId(projectId, userId);
    }

}
