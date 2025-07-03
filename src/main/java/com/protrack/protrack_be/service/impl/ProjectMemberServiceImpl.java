package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.ProjectMemberRequest;
import com.protrack.protrack_be.dto.response.ProjectMemberResponse;
import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.exception.AccessDeniedException;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.ProjectMemberMapper;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.ProjectMember;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import com.protrack.protrack_be.repository.ProjectMemberRepository;
import com.protrack.protrack_be.service.ProjectMemberService;
import com.protrack.protrack_be.service.ProjectPermissionService;
import com.protrack.protrack_be.service.ProjectService;
import com.protrack.protrack_be.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.ProjectMemberMapper.toResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired
    ProjectMemberRepository repo;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    ProjectPermissionService projectPermissionService;

    @Override
    @EnableSoftDeleteFilter
    public List<ProjectMemberResponse> getAll(){
        return repo.findAll().stream()
                .map(ProjectMemberMapper::toResponse)
                .collect(Collectors.toList());
    }
    private final ProjectMemberRepository memberRepository;

    @Override
    @EnableSoftDeleteFilter
    public Optional<ProjectMemberResponse> getById(ProjectMemberId id) {
        return repo.findById(id)
                .map(ProjectMemberMapper::toResponse);
    }

    @Override
    public Page<ProjectMemberResponse> getMembersOfProject(UUID projectId, Pageable pageable) {
        UUID userId = userService.getCurrentUser().getUserId();
        if (!isMember(projectId, userId)) {
            throw new AccessDeniedException("You are not permitted to view members of this project.");
        }
        return repo.findAllByProject_ProjectId(projectId, pageable)
                .map(ProjectMemberMapper::toResponse);
    }

    @Transactional
    @Override
    public ProjectMemberResponse create(ProjectMemberRequest request) {
        ProjectMember projectMember = new ProjectMember();
        Project project = projectService.getEntityById(request.getProjectId())
                .orElseThrow(() -> new NotFoundException("Can not find project"));
        User user = userService.getCurrentUser();

        if (projectService.hasProjectRight(project.getProjectId(), user.getUserId(), ProjectFunctionCode.EDIT_PROJECT)) {
            throw new com.protrack.protrack_be.exception.AccessDeniedException("You are not permitted to edit members of this project");
        }

        if (repo.existsByProject_ProjectIdAndUser_UserId(request.getProjectId(), user.getUserId())) {
            throw new BadRequestException("User is already a member of this project.");
        }

        ProjectMemberId id = new ProjectMemberId(project.getProjectId(), user.getUserId());

        projectMember.setId(id);
        projectMember.setProject(project);
        projectMember.setUser(user);
        projectMember.setRole(beautifyRole(request.getRole()));

        ProjectMember saved = repo.save(projectMember);

        return toResponse(saved);
    }

    @Transactional
    @Override
    @EnableSoftDeleteFilter
    public ProjectMemberResponse update(ProjectMemberId id, ProjectMemberRequest request){
        ProjectMember projectMember = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find project member"));
        Project project = projectService.getEntityById(request.getProjectId())
                .orElseThrow(() -> new NotFoundException("Can not find project"));
        User user = userService.getCurrentUser();

        if (projectService.hasProjectRight(project.getProjectId(), user.getUserId(), ProjectFunctionCode.EDIT_MEMBER)) {
            throw new com.protrack.protrack_be.exception.AccessDeniedException("You are not permitted to edit members of this project");
        }

        String newRole = request.getRole().trim().replaceAll("\\s+", "").toUpperCase();
        String currentRole = projectMember.getRole().trim().replaceAll("\\s+", "").toUpperCase();
        if (newRole.equals(currentRole)) {
            throw new BadRequestException("Role must be different from the current role.");
        }
        projectMember.setRole(beautifyRole(newRole));

        ProjectMember saved = repo.save(projectMember);

        return toResponse(saved);
    }

    @Transactional
    @Override
    public void delete(ProjectMemberId id){
        ProjectMember projectMember = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find project member"));
        Project project = projectMember.getProject();
        User user = userService.getCurrentUser();

        if (projectService.hasProjectRight(project.getProjectId(), user.getUserId(), ProjectFunctionCode.EDIT_MEMBER)) {
            throw new com.protrack.protrack_be.exception.AccessDeniedException("You are not permitted to edit members of this project");
        }

        if (isProjectOwner(project.getProjectId(), projectMember.getUser().getUserId())) {
            throw new com.protrack.protrack_be.exception.AccessDeniedException("You are not permitted to delete owner from the project");
        }

        projectPermissionService.deleteAllPermissionsOfUserInProject(project.getProjectId(), projectMember.getUser().getUserId());

        repo.deleteById(id);
    }

    @Transactional
    @Override
    public void leaveProject(UUID projectId) {
        UUID userId = userService.getCurrentUser().getUserId();
        ProjectMemberId id = new ProjectMemberId(projectId, userId);
        ProjectMember projectMember = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Project member not found"));

        if (isProjectOwner(projectId, userId)) {
            throw new BadRequestException("Owner is not able to leave at the moment. You can try delete the project instead.");
        }

        projectPermissionService.deleteAllPermissionsOfUserInProject(projectMember.getProject().getProjectId(), projectMember.getUser().getUserId());

        repo.deleteById(id);
    }


    @Override
    @EnableSoftDeleteFilter
    public boolean isProjectOwner(UUID projectId, UUID userId) {
        return memberRepository.existsByProject_ProjectIdAndUser_UserIdAndIsProjectOwnerTrue(projectId, userId);
    }

    @Override
    @EnableSoftDeleteFilter
    public boolean isMember(UUID projectId, UUID userId) {
        return memberRepository.existsByProject_ProjectIdAndUser_UserId(projectId, userId);
    }

    public static String beautifyRole(String input) {
        if (input == null) return null;
        String cleaned = input.trim().replaceAll("\\s+", " ");
        return WordUtils.capitalizeFully(cleaned); // "ux designer" → "Ux Designer"
    }
}
