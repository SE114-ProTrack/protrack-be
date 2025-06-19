package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskMemberResponse {

    private UUID taskId;
    private String taskName;

    private UUID userId;
    private String userFullName;
}
