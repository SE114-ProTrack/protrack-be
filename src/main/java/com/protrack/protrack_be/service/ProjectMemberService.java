package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.ProjectMemberRequest;
import com.protrack.protrack_be.dto.response.ProjectMemberResponse;
import com.protrack.protrack_be.model.ProjectMember;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberService {
    List<ProjectMemberResponse> getAll();
    Page<ProjectMemberResponse> getMembersOfProject(UUID projectId, Pageable pageable);
    Optional<ProjectMemberResponse> getById(ProjectMemberId id);
    ProjectMemberResponse create(ProjectMemberRequest request);
    ProjectMemberResponse update(ProjectMemberId id, ProjectMemberRequest request);
    void delete(ProjectMemberId id);
    void leaveProject(UUID projectId);
    boolean isProjectOwner(UUID projectId, UUID userId);
    boolean isMember(UUID projectId, UUID userId);
}
