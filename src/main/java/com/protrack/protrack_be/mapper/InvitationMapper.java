package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.InvitationResponse;
import com.protrack.protrack_be.model.Invitation;

public class InvitationMapper {

    public static InvitationResponse toResponse(Invitation entity) {
        return new InvitationResponse(
                entity.getInvitationId(),
                entity.getProject().getProjectId(),
                entity.getInvitationEmail(),
                entity.getInvitationToken(),
                entity.getInvitationDate(),
                entity.isAccepted()
        );
    }
}
