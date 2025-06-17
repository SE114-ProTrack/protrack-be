package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationResponse {
    private UUID invitationId;
    private UUID projectId;
    private String invitationEmail;
    private String invitationToken;
    private LocalDateTime invitationDate;
    private Integer invitationStatus;
}
