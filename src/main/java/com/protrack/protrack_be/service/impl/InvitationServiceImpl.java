package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.InvitationRequest;
import com.protrack.protrack_be.dto.response.InvitationResponse;
import com.protrack.protrack_be.mapper.InvitationMapper;
import com.protrack.protrack_be.model.Invitation;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.repository.InvitationRepository;
import com.protrack.protrack_be.repository.ProjectRepository;
import com.protrack.protrack_be.service.InvitationService;
import com.protrack.protrack_be.util.JwtUtil;
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
        String token = jwtUtil.generateInvitationToken(request.getInvitationEmail(), request.getProjectId());

        invitation.setProject(project);
        invitation.setInvitationEmail(request.getInvitationEmail());
        invitation.setInvitationToken(token);
        invitation.setInvitationDate(LocalDateTime.now());
        invitation.setAccepted(false);

        // Send email with token

        Invitation saved = repo.save(invitation);

        return toResponse(saved);
    }

    @Override
    public InvitationResponse accept(String token){
        Invitation invitation = repo.findByInvitationToken(token)
                .orElseThrow(() -> new RuntimeException("Can not find invitation"));

        invitation.setAccepted(true);

        // Email and validation

        Invitation saved = repo.save(invitation);

        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){ repo.deleteById(id); }
}
