package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityHistoryResponse {

    private UUID id;

    private UUID userId;
    private String userFullName;

    private UUID taskId;
    private String taskName;

    private String actionType;
    private String description;
    private LocalDateTime timestamp;
}
