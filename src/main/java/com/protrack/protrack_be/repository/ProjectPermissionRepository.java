package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.ProjectPermission;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPermissionRepository extends JpaRepository<ProjectPermission, ProjectPermissionId> {
}
