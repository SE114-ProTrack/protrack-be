package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private UUID userId;
    private String name;
    private String email;
    private String avatarUrl;
    private int projectCount;
    private int taskCount;
    private int contactCount;
}

