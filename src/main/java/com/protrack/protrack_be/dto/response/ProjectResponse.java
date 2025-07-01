package com.protrack.protrack_be.dto.response;

import com.protrack.protrack_be.model.Project;
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
    private String bannerUrl;
    private LocalDateTime createTime;
    private String creatorFullName;
    private int allTasks;
    private int completedTasks;
}