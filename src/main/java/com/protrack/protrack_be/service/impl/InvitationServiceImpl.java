package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.InvitationRequest;
import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.response.InvitationResponse;
import com.protrack.protrack_be.mapper.InvitationMapper;
import com.protrack.protrack_be.model.Invitation;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.ProjectMember;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.InvitationRepository;
import com.protrack.protrack_be.repository.ProjectMemberRepository;
import com.protrack.protrack_be.repository.ProjectRepository;
import com.protrack.protrack_be.service.AuthService;
import com.protrack.protrack_be.service.InvitationService;
import com.protrack.protrack_be.service.NotificationService;
import com.protrack.protrack_be.service.UserService;
import com.protrack.protrack_be.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
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
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public List<InvitationResponse> getAll(){
        return repo.findAll().stream()
                .map(InvitationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InvitationResponse> getById(UUID id){
        return repo.findById(id)
                .map(InvitationMapper::toResponse);
    }

    @Override
    public InvitationResponse create(InvitationRequest request){
        Invitation invitation = new Invitation();
        Project project = projectRepo.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Can not find project"));

        Optional<User> invitedUserOpt = userService.getUserByEmail(request.getInvitationEmail());

        String token = jwtUtil.generateInvitationToken(request.getInvitationEmail(), request.getProjectId());

        if (invitedUserOpt.isPresent()) {
            // Gửi notification trong app (nếu có hệ thống notify)
            notificationService.create(new NotificationRequest(
                    invitedUserOpt.get().getUserId(),
                    "INVITATION",
                    invitedUserOpt.get().getName() + " has invited you to \"" + project.getProjectName() + "\""
            ));
        } else {
            // Gửi email mời với token
            emailService.send(request.getInvitationEmail(), "INVITATION", token);
        }

        invitation.setProject(project);
        invitation.setInvitationEmail(request.getInvitationEmail());
        invitation.setInvitationToken(token);
        invitation.setInvitationDate(LocalDateTime.now());
        invitation.setAccepted(false);

        Invitation saved = repo.save(invitation);

        return toResponse(saved);
    }

    @Override
    public InvitationResponse accept(String token){
        Invitation invitation = repo.findByInvitationToken(token)
                .orElseThrow(() -> new RuntimeException("Can not find invitation"));

        if (invitation.isAccepted()) {
            throw new RuntimeException("Invitation has been accepted before");
        }

        Claims claims = jwtUtil.parseInvitationToken(token);
        String emailInToken = claims.getSubject();

        String currentUserEmail = userService.getCurrentUser().getAccount().getEmail();
        if (!emailInToken.equals(currentUserEmail)) {
            throw new RuntimeException("Token không thuộc về tài khoản hiện tại");
        }

        invitation.setAccepted(true);
        Invitation saved = repo.save(invitation);

        User user = userService.getCurrentUser();

        ProjectMember member = new ProjectMember();
        member.setProject(invitation.getProject());
        member.setUser(user);
        member.setIsProjectOwner(false);
        projectMemberRepository.save(member);

        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){ repo.deleteById(id); }
}
