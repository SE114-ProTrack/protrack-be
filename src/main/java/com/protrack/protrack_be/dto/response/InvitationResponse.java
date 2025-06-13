package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationResponse {
    private Long invitationId;
    private Long projectId;
    private String invitationEmail;
    private String invitationToken;
    private LocalDateTime invitationDate;
    private Integer invitationStatus;
}
