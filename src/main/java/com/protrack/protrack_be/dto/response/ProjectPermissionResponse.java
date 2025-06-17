package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionResponse {
    private UUID projectId;
    private UUID userId;
    private UUID functionId;
    private String functionName;
    private Boolean isActive;
}
