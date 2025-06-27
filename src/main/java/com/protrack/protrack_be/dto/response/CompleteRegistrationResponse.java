package com.protrack.protrack_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompleteRegistrationResponse {
    private String message;
    private boolean hasPendingInvitation;
}

