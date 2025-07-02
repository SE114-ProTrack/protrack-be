package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.InvitationRequest;
import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.response.InvitationResponse;
import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.InvitationMapper;
import com.protrack.protrack_be.model.*;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import com.protrack.protrack_be.repository.InvitationRepository;
import com.protrack.protrack_be.repository.ProjectMemberRepository;
import com.protrack.protrack_be.repository.ProjectPermissionRepository;
import com.protrack.protrack_be.repository.ProjectRepository;
import com.protrack.protrack_be.service.*;
import com.protrack.protrack_be.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.InvitationMapper.toResponse;

@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    InvitationRepository repo;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ProjectPermissionRepository projectPermissionRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    FunctionService functionService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectPermissionService projectPermissionService;

    @Override
    @EnableSoftDeleteFilter
    public List<InvitationResponse> getAll(){
        return repo.findAll().stream()
                .map(InvitationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<InvitationResponse> getById(UUID id){
        return repo.findById(id)
                .map(InvitationMapper::toResponse);
    }

    @Transactional
    @Override
    @EnableSoftDeleteFilter
    public InvitationResponse create(InvitationRequest request){
        if (!projectService.hasProjectRight(request.getProjectId(), userService.getCurrentUser().getUserId(), ProjectFunctionCode.INVITE_MEMBER)) {
            throw new AccessDeniedException("You are not permitted to invite new members");
        }

        Invitation invitation = new Invitation();
        Project project = projectRepo.findById(request.getProjectId())
                .orElseThrow(() -> new NotFoundException("Can not find project"));
        String ownerEmail = project.getCreatorId().getAccount().getEmail();
        if (request.getInvitationEmail().equalsIgnoreCase(ownerEmail)) {
            throw new BadRequestException("You cannot invite the project owner.");
        }

        Optional<Invitation> existing = repo.findByProjectAndInvitationEmail(project, request.getInvitationEmail());
        if (existing.isPresent()) {
            Invitation old = existing.get();
            if (old.isAccepted()) {
                throw new BadRequestException("User is already a member of the project.");
            }

            boolean tokenExpired = false;
            try {
                jwtUtil.parseInvitationToken(old.getInvitationToken());
            } catch (ExpiredJwtException e) {
                tokenExpired = true;
            }
            if (!tokenExpired) {
                throw new BadRequestException("User has already been invited. Please wait until the invitation expires.");
            }
            repo.delete(old);
        }


        Optional<User> invitedUserOpt = userService.getUserByEmail(request.getInvitationEmail());
        String token = jwtUtil.generateInvitationToken(request.getInvitationEmail(), request.getProjectId());

        if (invitedUserOpt.isPresent()) {
            if (projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(project.getProjectId(), invitedUserOpt.get().getUserId())) {
                throw new BadRequestException("This user is already a member of the project.");
            }
            notificationService.create(new NotificationRequest(
                    invitedUserOpt.get().getUserId(),
                    "INVITATION",
                    userService.getCurrentUser().getName() + " has invited you to \"" + project.getProjectName() + "\"",
                    "/invitations/accept?token=" + token
            ));
        } else {
            String acceptUrl = "https://frontend-url.com/invitations/accept?token=" + token;
            String content = "<p>You have been invited to <b>" + project.getProjectName() + "</b>.</p>"
                    + "<p>Click here to accept: <a href='" + acceptUrl + "'>Accept invite</a></p>";

            emailService.send(request.getInvitationEmail(), "INVITATION", content);
        }

        invitation.setProject(project);
        invitation.setInvitationEmail(request.getInvitationEmail());
        invitation.setInvitationToken(token);
        invitation.setInvitationDate(LocalDateTime.now());
        invitation.setAccepted(false);

        Invitation saved = repo.save(invitation);

        return toResponse(saved);
    }

    @Transactional
    @Override
    @EnableSoftDeleteFilter
    public InvitationResponse accept(String token){
        Invitation invitation = repo.findByInvitationToken(token)
                .orElseThrow(() -> new NotFoundException("Can not find invitation"));

        if (invitation.isAccepted()) {
            throw new RuntimeException("Invitation has been accepted before");
        }

        Claims claims = jwtUtil.parseInvitationToken(token);
        String emailInToken = claims.getSubject();

        String currentUserEmail = userService.getCurrentUser().getAccount().getEmail();
        if (!emailInToken.equals(currentUserEmail)) {
            throw new RuntimeException("Token doesn't belong to the current user");
        }

        invitation.setAccepted(true);
        Invitation saved = repo.save(invitation);

        User user = userService.getCurrentUser();
        Project project = invitation.getProject();

        ProjectMember member = new ProjectMember();
        ProjectMemberId projectMemberId = new ProjectMemberId(project.getProjectId(), user.getUserId());
        member.setId(projectMemberId);
        member.setProject(project);
        member.setUser(user);
        member.setIsProjectOwner(false);
        projectMemberRepository.save(member);

        projectPermissionService.grantDefaultPermissions(user.getUserId(), project.getProjectId());

        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){
        Invitation invitation = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Invitation not found"));
        Project project = invitation.getProject();
        UUID userId = userService.getCurrentUser().getUserId();

        if (!projectService.hasProjectRight(project.getProjectId(), userId, ProjectFunctionCode.INVITE_MEMBER)) {
            throw new AccessDeniedException("You are not permitted to delete this invitation");
        }

        repo.deleteById(id);
    }
}
