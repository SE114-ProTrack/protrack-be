package com.protrack.protrack_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    String token;
    UUID accountId;
    String name;
    String email;
    private boolean needCompleteProfile;
}
