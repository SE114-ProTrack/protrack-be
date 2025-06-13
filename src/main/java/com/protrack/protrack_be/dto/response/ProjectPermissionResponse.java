package com.protrack.protrack_be.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionResponse {
    private Long projectId;
    private Long userId;
    private Long functionId;
    private String functionName;
    private Boolean isActive;
}
