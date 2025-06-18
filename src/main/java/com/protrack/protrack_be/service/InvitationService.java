package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.InvitationRequest;
import com.protrack.protrack_be.dto.response.InvitationResponse;
import com.protrack.protrack_be.model.Invitation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationService {
    List<InvitationResponse> getAll();
    Optional<InvitationResponse> getById(UUID id);
    InvitationResponse create(InvitationRequest request);
    InvitationResponse update(UUID id, InvitationRequest request);
    void delete(UUID id);
}
