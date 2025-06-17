package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private UUID projectId;
    private String projectName;
    private String description;
    private LocalDateTime createTime;
    private String creatorFullName;
}