package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long projectId;
    private String projectName;
    private String description;
    private LocalDateTime createTime;
    private String creatorFullName;
}