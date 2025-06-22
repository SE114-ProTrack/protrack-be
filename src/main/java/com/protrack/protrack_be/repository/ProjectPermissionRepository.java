package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.ProjectPermission;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectPermissionRepository extends JpaRepository<ProjectPermission, ProjectPermissionId> {
    boolean existsByUser_UserIdAndProject_ProjectIdAndFunction_FunctionCodeAndIsActiveTrue(
            UUID userId, UUID projectId, String functionCode);
}
