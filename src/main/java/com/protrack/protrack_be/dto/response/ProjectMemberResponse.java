package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberResponse {

    private UUID projectId;
    private UUID userId;
    private String userFullName;
    private Boolean isProjectOwner;
}
