package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.dto.request.ProjectPermissionRequest;
import com.protrack.protrack_be.dto.response.ProjectPermissionResponse;
import com.protrack.protrack_be.mapper.ProjectPermissionMapper;
import com.protrack.protrack_be.model.Function;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.ProjectPermission;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import com.protrack.protrack_be.repository.ProjectPermissionRepository;
import com.protrack.protrack_be.service.*;
import lombok.RequiredArgsConstructor;
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

        projectPermission.setProject(project);
        projectPermission.setUser(user);
        projectPermission.setFunction(function);
        projectPermission.setIsActive(request.getIsActive());

        ProjectPermission saved = repo.save(projectPermission);

        return toResponse(saved);
    }

    @Override
    public void update(UUID projectId, UUID userId, Map<String, Boolean> permissionMap) {

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
    public void delete(ProjectPermissionId id){ repo.deleteById(id); }

    @Override
    public boolean hasPermission(UUID userId, UUID projectId, ProjectFunctionCode functionCode) {
        return permissionRepository.existsByUser_UserIdAndProject_ProjectIdAndFunction_FunctionCodeAndIsActiveTrue(
                userId, projectId, functionCode.name()
        );
    }
}
