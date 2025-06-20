package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.repository.ProjectPermissionRepository;
import com.protrack.protrack_be.service.ProjectPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectPermissionServiceImpl implements ProjectPermissionService {

    private final ProjectPermissionRepository permissionRepository;

    @Override
    public boolean hasPermission(UUID userId, UUID projectId, ProjectFunctionCode functionCode) {
        return permissionRepository.existsByUser_UserIdAndProject_ProjectIdAndFunction_FunctionCodeAndIsActiveTrue(
                userId, projectId, functionCode.name()
        );
    }
}

