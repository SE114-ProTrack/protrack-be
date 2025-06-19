package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalProductivityResponse {

    private UUID userId;
    private String userFullName;

    private UUID projectId;
    private String projectName;

    private Integer completedTasks;
    private LocalDateTime lastUpdated;
}
