package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.InvitationRequest;
import com.protrack.protrack_be.dto.response.InvitationResponse;
import com.protrack.protrack_be.mapper.InvitationMapper;
import com.protrack.protrack_be.model.Invitation;
import com.protrack.protrack_be.repository.InvitationRepository;
import com.protrack.protrack_be.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.InvitationMapper.toResponse;

public class InvitationServiceImpl implements InvitationService {

    @Autowired
    InvitationRepository repo;

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
        return new InvitationResponse();
    }

    @Override
    public InvitationResponse update(UUID id, InvitationRequest request){
        return new InvitationResponse();
    }

    @Override
    public void delete(UUID id){ repo.deleteById(id); }
}
