package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.ProjectMember;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    boolean existsByProject_ProjectIdAndUser_UserId(UUID projectId, UUID userId);
    boolean existsByProject_ProjectIdAndUser_UserIdAndIsProjectOwnerTrue(UUID projectId, UUID userId);
}
