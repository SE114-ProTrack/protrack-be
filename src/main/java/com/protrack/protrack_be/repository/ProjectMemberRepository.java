package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.ProjectMember;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    boolean existsByProject_ProjectIdAndUser_UserId(UUID projectId, UUID userId);
    boolean existsByProject_ProjectIdAndUser_UserIdAndIsProjectOwnerTrue(UUID projectId, UUID userId);
    Page<ProjectMember> findAllByProject_ProjectId(UUID projectId, Pageable pageable);
    List<ProjectMember> findAllByProject_ProjectId(UUID projectId);
}
